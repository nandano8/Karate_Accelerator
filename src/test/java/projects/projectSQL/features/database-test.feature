Feature: PostgreSQL Database Testing

Background:
  * def dbUrl = 'jdbc:postgresql://localhost:5432/Karate Test'
  * def dbUser = 'postgres'
  * def dbPassword = 'sql_test'
  * db.connect(dbUrl, dbUser, dbPassword)

Scenario: SELECT query
  * def result = db.select('SELECT * FROM public."KarateDb" WHERE id = ? AND "Status" = ?', [3, 1])
  * match result[0].Name == 'Charli'
  * db.disconnect()

Scenario: INSERT
  * def rowsAffected = db.insert('INSERT INTO "KarateDb" ("Name", "Department", "Role", "Status") VALUES (?, ?, ?, ?)', ['Golf', 'HR', 'HRBP', 1])
  * match rowsAffected == 1
  * db.disconnect()

Scenario: UPDATE
  * def rowsAffected = db.update('UPDATE "KarateDb" SET "Name" = ? WHERE "Role" = ?', ['Golf', 'HRBP'])
  * match rowsAffected == 1
  * db.disconnect()

Scenario: DELETE
  * def rowsAffected = db.delete('DELETE FROM "KarateDb" WHERE "Name" = ? AND "Status" = ?', ['Golf', 1])
  * match rowsAffected == 1
  * db.disconnect()