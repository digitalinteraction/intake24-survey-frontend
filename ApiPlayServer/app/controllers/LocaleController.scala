/*
This file is part of Intake24.

Copyright 2015, 2016 Newcastle University.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package controllers

import play.api.mvc.Controller
import play.api.libs.json.Json
import play.api.mvc.Action
import uk.ac.ncl.openlab.intake24.nutrients.EnergyKcal
import play.api.libs.json.JsError
import scala.concurrent.Future
import upickle.default._
import com.oracle.webservices.internal.api.message.ContentType
import play.api.http.ContentTypes
import javax.inject.Inject
import be.objectify.deadbolt.scala.DeadboltActions
import be.objectify.deadbolt.core.PatternType
import uk.ac.ncl.openlab.intake24.services.foodindex.FoodIndex
import uk.ac.ncl.openlab.intake24.services.LocaleManagementService
import security.Permissions
import security.Roles


class LocaleController @Inject() (service: LocaleManagementService, deadbolt: DeadboltActions) extends Controller {
  
  def list = deadbolt.Restrict(List(Array(Roles.superuser))) {
    Action {
      Ok(write(service.list)).as(ContentTypes.JSON)
    }
  }
  
  def get(id: String) = deadbolt.Restrict(List(Array(Roles.superuser))) {
    Action {
      service.get(id) match {
        case None => NotFound
        case Some(locale) => Ok(write(locale)).as(ContentTypes.JSON)
      }
    }
  }
}
