# SmartEnergyRecommender_App
 Android Application that tracks user's energy consumption across suburbs as well as provides reports based on selected timeframe
 
 Server - 
 Restful webservices in JAVA created that can be invoked from any client through HTTP
 Includes both static and dynamic queries.
 Database - Derby Database
 
 Client - 
 Android App that recommends usage data and indicates when usage is above a certain level.
 It invokes queries through HTTP calls asynchronously and performs tasks in the background
 Maps functionality is provided to see usages of different houses on the map
 Reports can be viewed through line, bar and pie charts for different months.
 Database - SqlLite DB
