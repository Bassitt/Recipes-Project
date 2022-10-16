# Recipes
# About the Service
The application is just a simple multi-user RESTful service for kitchen recipes sharing. That allows to perform CRUD operations with recipes and provides Basic HTTP authentication. It uses a H2 database to store the data. You can also use any other relational database. If your database connection properties work, you can call some REST endpoints defined in localhost on port 8080.

<b>Description</b>

Imagine a service that supports the registration process, can handle a lot of users, and each of them can add their own recipes. Also, a user can update or delete only their recipes but can view recipes added by other users. In this stage, you will implement all this functionality with Spring Boot Security.

<b>Objectives</b>
<ul><li>
New endpoint POST /api/register receives a JSON object with two fields: email (string), and password (string). If a user with a specified email does not exist, the program saves (registers) the user in a database and responds with 200 (Ok). If a user is already in the database, respond with the 400 (Bad Request) status code. Both fields are required and must be valid: email should contain @ and . symbols, password should contain at least 8 characters and shouldn't be blank. If the fields do not meet these restrictions, the service should respond with 400 (Bad Request). Also, do not forget to use an encoder before storing a password in a database. BCryptPasswordEncoder is a good choice. </li>

<li>Include the Spring Boot Security dependency and configure access to the endpoints – all implemented endpoints (except /api/register) should be available only to the registered and then authenticated and authorized via HTTP Basic auth users. Otherwise, the server should respond with the 401 (Unauthorized) status code. </li>
<li>Add additional restrictions – only an author of a recipe can delete or update a recipe. If a user is not the author of a recipe, but they try to carry out the actions mentioned above, the service should respond with the 403 (Forbidden) status code.
For testing purposes, POST/actuator/shutdown should be available without authentication. </li>

# Examples
<b>Example 1:</b> POST /api/recipe/new request without authentication

<pre>{
  "name": "Fresh Mint Tea",
   "category": "beverage",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
} </pre>
Status code: 401 (Unauthorized)

<b>Example 2:</b> POST /api/register request without authentication
<pre>
{
   "email": "Cook_Programmer@somewhere.com",
   "password": "RecipeInBinary"
}</pre>
Status code: 200 (Ok)

Further POST /api/recipe/new request with basic authentication; email (login): Cook_Programmer@somewhere.com, and password: RecipeInBinary
<pre>
{
   "name": "Mint Tea",
   "category": "beverage",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}</pre>
Response:
<pre>
{
   "id": 1
}</pre>
Further PUT /api/recipe/1 request with basic authentication; email (login): Cook_Programmer@somewhere.com, password: RecipeInBinary
<pre>
{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
} </pre>
Status code: 204 (No Content)

Further GET /api/recipe/1 request with basic authentication; email (login): Cook_Programmer@somewhere.com, password: RecipeInBinary

Response:
<pre>
{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "date": "2020-01-02T12:11:25.034734",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}</pre>

<b>Example 3:</b> POST /api/register request without authentication
<pre>
{
   "email": "CamelCaseRecipe@somewhere.com",
   "password": "C00k1es."
}</pre>
Status code: 200 (Ok)

Further response for the GET /api/recipe/1 request with basic authentication; email (login): CamelCaseRecipe@somewhere.com, password: C00k1es.
<pre>
{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "date": "2020-01-02T12:11:25.034734",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}</pre>
Further PUT /api/recipe/1 request with basic authentication; email (login): CamelCaseRecipe@somewhere.com, password: C00k1es.
<pre>
{
   "name": "Warming Ginger Tea",
   "category": "beverage",
   "description": "Ginger tea is a warming drink for cool weather, ...",
   "ingredients": ["1 inch ginger root, minced", "1/2 lemon, juiced", "1/2 teaspoon manuka honey"],
   "directions": ["Place all ingredients in a mug and fill with warm water (not too hot so you keep the beneficial honey compounds in tact)", "Steep for 5-10 minutes", "Drink and enjoy"]
}</pre>
Status code: 403 (Forbidden)

Further DELETE /api/recipe/1 request with basic authentication; email (login): CamelCaseRecipe@somewhere.com, password: C00k1es.

Status code: 403 (Forbidden)
