# Read Me 
curl -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d "{\"username\": \"admin\", \"password\": \"admin123\"}"

curl -X GET http://localhost:8080/auth/protected -H "Authorization: Bearer YOUR_JWT_TOKEN"
curl -X GET http://localhost:8080/protected -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcyNzg3NTAwMCwiZXhwIjoxNzI3OTExMDAwfQ.hKe-1BN49pVDFX1Rdkifpn9d6iZu45GULx6JSWF4tSA"
