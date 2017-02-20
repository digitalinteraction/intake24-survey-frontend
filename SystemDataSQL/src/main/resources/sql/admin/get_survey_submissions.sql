SELECT id, survey_id, user_id, start_time, end_time, log,
    ARRAY(SELECT ARRAY[name, value] FROM survey_submission_custom_fields AS cf WHERE cf.survey_submission_id = ss.id) AS submission_custom_fields,
    ARRAY(SELECT ARRAY[name, value] FROM survey_submission_user_custom_fields AS ucf WHERE ucf.survey_submission_id = ss.id) AS user_custom_fields
FROM survey_submissions AS ss
WHERE survey_id={survey_id} AND start_time>{time_from} AND end_time<{time_to}
ORDER BY end_time ASC
OFFSET {offset} LIMIT {limit}