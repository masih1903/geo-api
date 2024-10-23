# API Documentation for AtlasAPI

AtlasAPI is a RESTful API designed to fetch and manage country-related data. It provides endpoints to retrieve
information about countries based on criteria like name, region, currency, language, and capital city. Users can access
population data and find countries by driving side. CRUD operations on country records are restricted to admin users
only, ensuring secure management of the data. The API returns data in JSON format, with error handling for a smooth user
experience.

This document describes the endpoints of the AtlasAPI for fetching and managing country-related data. Each method is
explained with its URL, request body (if any), response format, and possible errors.

| **Method** | **URL**                                        | **Request Body (JSON)** | **Response (JSON)**                                                                                                                                                                                                                                               | **Error (e)**                                                                                  |
|------------|------------------------------------------------|-------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|
| GET        | `/api/countries/name/{name}`                   | None                    | ```[ { "name": {"common": String, "official": String}, "capital": [String], "region": String, "population": Number, "languages": {"language": String}, "currencies": {"currency": {"name": String, "symbol": String}}, "car": {"side": "left" or "right"} } ]```  | **e1**: `{ "status": 404, "msg": "Country with name '{name}' not found" }`                     |
| GET        | `/api/countries/currency/{currency}`           | None                    | ```[ { "name": {"common": String, "official": String}, "capital": [String], "region": String, "population": Number, "languages": {"language": String}, "currencies": {"currency": {"name": String, "symbol": String}}, "car": {"side": "left" or "right"} } ]```  | **e1**: `{ "status": 404, "msg": "No countries found using the currency '{currency}'" }`       |
| GET        | `/api/countries/language/{language}`           | None                    | ```[ { "name": {"common": String, "official": String}, "capital": [String], "region": String, "population": Number, "languages": {"language": String}, "currencies": {"currency": {"name": String, "symbol": String}}, "car": {"side": "left" or "right"} } ]```  | **e1**: `{ "status": 404, "msg": "No countries found with the language '{language}'" }`        |
| GET        | `/api/countries/capital/{capital}`             | None                    | ```[ { "name": {"common": String, "official": String}, "capital": [String], "region": String, "population": Number, "languages": {"language": String}, "currencies": {"currency": {"name": String, "symbol": String}}, "car": {"side": "left" or "right"} } ]```  | **e1**: `{ "status": 404, "msg": "No country found with the capital '{capital}'" }`            |
| GET        | `/api/countries/region/{region}`               | None                    | ```[ { "name": {"common": String, "official": String}, "capital": [String], "region": String, "population": Number, "languages": {"language": String}, "currencies": {"currency": {"name": String, "symbol": String}}, "car": {"side": "left" or "right"} } ]```  | **e1**: `{ "status": 404, "msg": "No countries found in the region '{region}'" }`              |
| GET        | `/api/countries/top-population`                | None                    | ```[ { "name": {"common": String, "official": String}, "capital": [String], "region": String, "population": Number, "languages": {"language": String}, "currencies": {"currency": {"name": String, "symbol": String}}, "car": {"side": "left" or "right"} } ]```  | **e1**: `{ "status": 404, "msg": "Could not retrieve population data" }`                       |
| GET        | `/api/countries/lowest-population`             | None                    | ```[ { "name": {"common": String, "official": String}, "capital": [String], "region": String, "population": Number, "languages": {"language": String}, "currencies": {"currency": {"name": String, "symbol": String}}, "car": {"side": "left" or "right"} } ]```  | **e1**: `{ "status": 404, "msg": "Could not retrieve population data" }`                       |
| GET        | `/api/countries/car/drivingside/{drivingside}` | None                    | ```[ { "name": {"common": String, "official": String}, "capital": [String], "region": String, "population": Number, "languages": {"language": String}, "currencies": {"currency": {"name": String, "symbol": String}}, "car": {"side": "left" or "right"} } ]```  | **e1**: `{ "status": 404, "msg": "Could not retrieve driving data for left-side countries" }`  |
| GET        | `/api/countries`                               | None                    | ```[ { "name": {"common": String, "official": String}, "capital": [String], "region": String, "population": Number, "languages": {"language": String}, "currencies": {"currency": {"name": String, "symbol": String}}, "car": {"side": "left" or "right"} } ]```  | **e1**: `{ "status": 404, "msg": "No countries found" }`                                       |

---

## **CRUD Operations**

| **Method** | **URL**               | **Request Body (JSON)**                                                                                                                                                                                                                                                                                                                     | **Response (JSON)**                                                                                                                                                                                                                                               | **Error (e)**                                                          |
|------------|-----------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------|
| POST       | `/api/countries`      | ```{ "name": {"common": "Country Name", "official": "Official Country Name"}, "capital": ["Capital City"], "region": "Region Name", "population": 1000000, "languages": {"language": "Language Name"}, "currencies": {"currency": {"name": "Currency Name", "symbol": "Currency Symbol"}}, "car": {"side": "left"} }```                     | ```{ "id": 1, "name": {"common": "Country Name", "official": "Official Country Name"}, "capital": ["Capital City"], "region": "Region Name", "population": 1000000, "languages": {"language": "Language Name"}, "currencies": {...}, "car": {"side": "left"} }``` | **e2**: `{ "status": 400, "msg": "Field 'name' is required" }`         |
| GET        | `/api/countries/{id}` | None                                                                                                                                                                                                                                                                                                                                        | ```{ "id": 1, "name": {"common": "Country Name", "official": "Official Country Name"}, "capital": ["Capital City"], "region": "Region Name", "population": 1000000, "languages": {"language": "Language Name"}, "currencies": {...}, "car": {"side": "left"} }``` | **e1**: `{ "status": 404, "msg": "Country with id '{id}' not found" }` |
| PUT        | `/api/countries/{id}` | ```{ "name": {"common": "Country Name", "official": "Updated Official Country Name"}, "capital": ["Updated Capital"], "region": "Updated Region", "population": 1500000, "languages": {"language": "Updated Language"}, "currencies": {"currency": {"name": "Updated Currency", "symbol": "Updated Symbol"}}, "car": {"side": "right"} }``` | ```{ "id": 1, "name": {"common": "Country Name", "official": "Updated Official Country Name"}, "capital": ["Updated Capital"], "region": "Updated Region", "population": 1500000, "languages": {...}, "currencies": {...}, "car": {"side": "right"} }```          | **e1**: `{ "status": 404, "msg": "Country with id '{id}' not found" }` |
| DELETE     | `/api/countries/{id}` | None                                                                                                                                                                                                                                                                                                                                        | ```{ "status": 204, "msg": "Country with id '{id}' successfully deleted" }```                                                                                                                                                                                     | **e1**: `{ "status": 404, "msg": "Country with id '{id}' not found" }` |

---

## Error Format

All errors will follow this format:

```json
{
  "status": Number,
  "msg": "Error message describing the issue"
}

```

Explanation of Errors
e1: Occurs when the requested resource (country by name, ID, currency, etc.) cannot be found. This results in a 404 Not
Found error.
e2: Occurs when required fields such as "name" are missing in a POST or PUT request. This results in a 400 Bad Request
error.
