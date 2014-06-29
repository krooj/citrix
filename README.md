docuserv
========

EXECUTION
========
mvn clean install 

TESTS
========
```text
curl -i -v -X POST -H "x-documentid:sample.txt" --data-binary "@sample.txt" -H "Content-Type: application/octet-stream" http://localhost:8080/storage/documents
curl -i -v -X PUT -H "x-documentid:sample.txt" --data-binary "@sample.txt" -H "Content-Type: application/octet-stream" http://localhost:8080/storage/documents
curl -v http://localhost:8080/storage/documents/sample.txt
curl -v -X DELETE http://localhost:8080/storage/documents/sample.txt

N.B.: The filename POST'ed becomes the documentId to identify the resource.
```
