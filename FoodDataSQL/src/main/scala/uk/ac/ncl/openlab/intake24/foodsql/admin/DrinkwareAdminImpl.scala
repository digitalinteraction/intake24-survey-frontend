package uk.ac.ncl.openlab.intake24.foodsql.admin

import uk.ac.ncl.openlab.intake24.DrinkwareHeader
import anorm.SQL
import anorm.Macro
import anorm.sqlToSimple
import uk.ac.ncl.openlab.intake24.foodsql.user.DrinkwareUserImpl
import uk.ac.ncl.openlab.intake24.services.fooddb.errors.DatabaseError
import uk.ac.ncl.openlab.intake24.services.fooddb.admin.DrinkwareAdminService
import uk.ac.ncl.openlab.intake24.DrinkwareSet
import org.slf4j.LoggerFactory
import anorm.NamedParameter
import anorm.BatchSql

trait DrinkwareAdminImpl extends DrinkwareAdminService with DrinkwareUserImpl {

  private val logger = LoggerFactory.getLogger(classOf[DrinkwareAdminImpl])

  def allDrinkware(): Either[DatabaseError, Seq[DrinkwareHeader]] = tryWithConnection {
    implicit conn =>
      Right(SQL("""SELECT id, description FROM drinkware_sets ORDER BY description ASC""").executeQuery().as(Macro.namedParser[DrinkwareHeader].*))
  }

  def deleteAllDrinkwareSets(): Either[DatabaseError, Unit] = tryWithConnection {
    implicit conn =>
      logger.info("Deleting existing drinkware definitions")

      SQL("DELETE FROM drinkware_sets").execute()

      Right(())
  }

  def createDrinkwareSets(sets: Seq[DrinkwareSet]): Either[DatabaseError, Unit] = tryWithConnection {
    implicit conn =>

      if (!sets.isEmpty) {
        conn.setAutoCommit(false)

        logger.info("Writing " + sets.size + " drinkware sets to database")
        val drinkwareParams = sets.flatMap {
          set =>
            Seq[NamedParameter]('id -> set.id, 'description -> set.description, 'guide_image_id -> set.guide_id)
        }

        BatchSql("""INSERT INTO drinkware_sets VALUES ({id}, {description}, {guide_image_id})""", drinkwareParams).execute()

        val drinkwareScaleParams = sets.foreach {
          set =>
            set.scaleDefs.foreach {
              scale =>
                val scaleId = SQL("""INSERT INTO drinkware_scales VALUES (DEFAULT, {drinkware_set_id}, {width}, {height}, {empty_level}, {full_level}, {choice_id}, {base_image_url}, {overlay_image_url})""")
                  .on('drinkware_set_id -> set.id, 'width -> scale.width, 'height -> scale.height, 'empty_level -> scale.emptyLevel, 'full_level -> scale.fullLevel,
                    'choice_id -> scale.choice_id, 'base_image_url -> scale.baseImage, 'overlay_image_url -> scale.overlayImage)
                  .executeInsert()
                  .get

                val volumeSampleParams = scale.vf.sortedSamples.map {
                  case (fill, volume) =>
                    Seq[NamedParameter]('scale_id -> scaleId, 'fill -> fill, 'volume -> volume)
                }

                if (!volumeSampleParams.isEmpty) {
                  logger.info("Writing " + volumeSampleParams.size + " volume sample records to database")
                  BatchSql("""INSERT INTO drinkware_volume_samples VALUES (DEFAULT, {scale_id}, {fill}, {volume})""", volumeSampleParams).execute()
                } else
                  logger.warn("Drinkware file contains no volume samples")
            }
        }

        conn.commit()
        Right(())
      } else {
        logger.warn("Drinkware file contains no records")
        Right(())
      }
  }
}