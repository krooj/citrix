docuserv
========

EXECUTION
========
mvn clean install tomcat7:exec-war && java -jar target/docuserv-1.0-war-exec.jar

TESTS
========
```text
curl -v -X POST -F document=@sample http://localhost:8080/storage/documents
curl -v -X PUT -F document=@sample http://localhost:8080/storage/documents
curl -v http://localhost:8080/storage/documents/sample
curl -v -X DELETE http://localhost:8080/storage/documents/sample

N.B.: The filename POST'ed becomes the documentId to identify the resource.
```
