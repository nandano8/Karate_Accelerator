Feature: PostgreSQL Database Testing

Background:
  * def dbUrl = 'jdbc:postgresql://localhost:5432/Karate Test'
  * def dbUser = 'postgres'
  * def dbPassword = 'sql_test'
  * def allureHelper = read('classpath:utilities/allure-helper.js')
  * db.connect(dbUrl, dbUser, dbPassword)
  * call allureHelper.addTextAttachment('Database Connection', 'Connected to: ' + dbUrl)

Scenario: SELECT query
  * call allureHelper.addTestStep('SELECT Query', 'Executing SELECT query with Status = 1')
  * def startTime = Date.now()
  * def result = db.select('SELECT * FROM public."KarateDb" WHERE "Status" = ?', [1])
  * call allureHelper.addPerformanceInfo('SELECT Query', startTime)
  * call allureHelper.addJsonAttachment('Query Results', result)
  * match result.size() > 0
  * db.disconnect()

Scenario: INSERT
  * call allureHelper.addTestStep('INSERT Operation', 'Inserting new record: Arijit, HR, QA, 1')
  * def startTime = Date.now()
  * def rowsAffected = db.insert('INSERT INTO "KarateDb" ("Name", "Department", "Role", "Status") VALUES (?, ?, ?, ?)', ['Arijit', 'HR', 'QA', 1])
  * call allureHelper.addPerformanceInfo('INSERT Operation', startTime)
  * call allureHelper.addTextAttachment('Insert Result', 'Rows affected: ' + rowsAffected)
  * match rowsAffected == 1
  * db.disconnect()

Scenario Outline: UPDATE with parameters
  * call allureHelper.addTestStep('UPDATE Operation', 'Updating <updateColumn> to <newValue> where <whereColumn> = <whereValue>')
  * def updateQuery = 'UPDATE "KarateDb" SET "<updateColumn>" = ? WHERE "<whereColumn>" = ?'
  * def startTime = Date.now()
  * def rowsAffected = db.update(updateQuery, ['<newValue>', '<whereValue>'])
  * call allureHelper.addPerformanceInfo('UPDATE Operation', startTime)
  * call allureHelper.addTextAttachment('Update Result', 'Rows affected: ' + rowsAffected)
  * match rowsAffected >= 0
  * db.disconnect()

  Examples:
    | updateColumn | newValue | whereColumn | whereValue |
    | Name         | TestUser | Role        | QA         |
    | Department   | IT       | Name        | Arijit     |
    | Status       | 0        | Department  | HR         |

Scenario: DELETE
  * call allureHelper.addTestStep('DELETE Operation', 'Deleting records where Status = 0')
  * def startTime = Date.now()
  * def rowsAffected = db.delete('DELETE FROM "KarateDb" WHERE "Status" = ?', [0])
  * call allureHelper.addPerformanceInfo('DELETE Operation', startTime)
  * call allureHelper.addTextAttachment('Delete Result', 'Rows affected: ' + rowsAffected)
  * match rowsAffected >= 0
  * db.disconnect()