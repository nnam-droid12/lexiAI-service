## LexiAI Service - Intelligent Document Processing with Azure AI
### LexiAI is a powerful AI-driven service that automates document processing, translation, and text-to-speech conversion using Azure AI Services. It enhances efficiency by extracting insights from documents, translating text, and converting it into speech for accessibility.

 ### Features
-  Upload and store documents securely using Azure Blob Storage
- Extract text from documents with Azure Document Intelligence
- Translate extracted text into multiple languages via Azure Translator
- Convert translated text to speech with Azure Speech Services
- Scalable and efficient, leveraging Azure Cosmos DB for PostgreSQL

### Technologies Used
- Backend: Java Spring Boot
- Frontend: React.js
- Cloud Services:
  1.Azure Blob Storage (Document Uploads)
  2.Azure Document Intelligence (Text Extraction)
  3.Azure Translator (Multilingual Support)
  4.Azure Speech Services (Text-to-Speech)
  5.Azure Cosmos DB for PostgreSQL (Data Storage)
  6.Build & Deployment: Docker, Azure Functions
### Setup & Installation
1 Clone the Repository
git clone https://github.com/your-repo-name/lexiai-service.git
cd lexiai-service
2. Configure Azure Credentials
Replace placeholders in application.yml with your Azure keys:

yaml
Copy
Edit
azure:
  blob-storage:
    connection-string: "<YOUR_AZURE_BLOB_CONNECTION_STRING>"
  document-intelligence:
    endpoint: "<YOUR_AZURE_DOCUMENT_INTELLIGENCE_ENDPOINT>"
    key: "<YOUR_AZURE_DOCUMENT_INTELLIGENCE_KEY>"
  translator:
    endpoint: "<YOUR_AZURE_TRANSLATOR_ENDPOINT>"
    key: "<YOUR_AZURE_TRANSLATOR_KEY>"
  speech-services:
    endpoint: "<YOUR_AZURE_SPEECH_ENDPOINT>"
    key: "<YOUR_AZURE_SPEECH_KEY>"
  database:
    url: "<YOUR_COSMOS_DB_CONNECTION_STRING>"
3. Run the Backend
./mvnw spring-boot:run
4. Start the Frontend (if applicable)
cd frontend
npm install
npm start
### API Endpoints
Method	Endpoint	Description
POST	/api/v1/document/upload	Upload a document to Azure Blob Storage
GET	/api/v1/document/{fileName}	Retrieve a document by file name
GET	/api/v1/document/all	Get a list of uploaded documents
POST	/api/v1/document/analyze	Extract text from a document
POST	/api/v1/document/translate	Translate extracted text
POST	/api/v1/document/speech	Convert translated text to speech
### Testing Instructions
Upload a Document:

Use Postman or the frontend UI to send a POST request to /api/v1/document/upload with a file.
Verify that the response contains the file URL from Azure Blob Storage.
Extract Text:

Send a POST request to /api/v1/document/analyze with the file URL.
Ensure that text is extracted successfully.
Translate Text:

Call /api/v1/document/translate with the extracted text and target language.
Confirm the response contains translated text.
Convert to Speech:

Send a POST request to /api/v1/document/speech with translated text.
Verify that an audio file is returned.
📜 License
This project is open-source and available under the MIT License.

🤝 Contributing
Feel free to submit issues, feature requests, or pull requests!

🚀 LexiAI – Transforming Documents with AI!
