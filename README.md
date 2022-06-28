This is the back end of the application. 

It uses spring boot and works as an API rest applications. It only allows Get methods and for now it only uses getPokedex and getSpecifics

The response is formatted as a JSON Object

getPokedex:

Returns 12 pokemons in order with their basic informations like Types, abilites, weight and image url

Parameters:
  offset: the starting pokemon from the list deployed. IE offset = 3 brings 12 pokemon starting from venasaur.
  
 
getSpecifics:

  Returns same information but with the evolution chain as well with their respective images.
  
  
  There is a frontend application deveolped in Vue.js which will also be in this repository
