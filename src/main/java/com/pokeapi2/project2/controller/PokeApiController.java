package com.pokeapi2.project2.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Response;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokeapi2.project2.entities.Ability;
import com.pokeapi2.project2.entities.EvolucionChain;
import com.pokeapi2.project2.entities.Pokemon;
import com.pokeapi2.project2.entities.PokemonSpecifics;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET})
public class PokeApiController {

	 public static final String API_URL = "https://pokeapi.co/api/v2/";
	
	@RequestMapping("/")
	public String home() {
	
		return "Welcome to the Pokemon Rest Api </br></br> "
				+ "To get a list of all the pokemons call getPokedex with offset as a REQUIRED parameter </br>"
				+ "offset refers to the pokemon you want to start from. </br>"
				+ "Example: https://project2-1656302484892.azurewebsites.net//getPokedex?offset=20 </br>"
				+ "For performance the api will list up to 12 pokemon at once, you can change the pokemon list by changing the offset </br>"
				+ "to get the specifics of a pokemon you will need to call 'getSpecific' </br>"
				+ "parameter: 'pokemonId' which refers to the Id of the pokemon to get the information"
				+ "</br>"
				+ "</br>"
				+ "The response will be a Json object</br>"
				+ "</br>"
				+ "A frontend application was develop using Vue.js it will be in the Repository and the url to use it is: https://mysterious-basin-99418.herokuapp.com/";
	}
	@Cacheable(cacheNames ="Pokedex", key="#offset")
	@GetMapping(value = "/getPokedex")
	 public String GetPokedex (HttpSession session, Model model, @RequestParam(value = "offset", required = true) String offset) {
				String page = "";
				ArrayList<Pokemon> pokemonList = new ArrayList<>();
				Pokemon pokemonToAdd = new Pokemon();
	            if (StringUtils.isBlank(offset)) {
	            	page = "0";
	            }else{
	            	page = offset;
	                }
	        	URL url;
	        	StringBuffer response = new StringBuffer();
	            try {
	            url = new URL(API_URL+"pokemon?limit=12&offset="+page);
				HttpURLConnection http = (HttpURLConnection)url.openConnection();
				http.setRequestProperty("Accept", "application/json");
				int responseCode = http.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) { // success
					BufferedReader in = new BufferedReader(new InputStreamReader(
							http.getInputStream()));
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();
					
					// print result
				} else {
					System.out.println("GET request not worked");
				}
				http.disconnect();
				JSONObject json = new JSONObject(response.toString());
				JSONArray arrayJson = json.getJSONArray("results");
				for(int i = 0; i< arrayJson.length();i++) {
					JSONObject pokemonJson = arrayJson.getJSONObject(i);
					pokemonToAdd = buildPokemonForPokedex(pokemonJson);
					pokemonList.add(pokemonToAdd);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.print(e.getMessage());
				return "Hubo un error de conexion con la PokeApi";
			}	
	            
	            ObjectMapper mapper = new ObjectMapper();
	            //Converting the Object to JSONString
	            String jsonString = "";
				try {
					jsonString = mapper.writeValueAsString(pokemonList);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					
					e.printStackTrace();
					return "Hubo un error al mapear el objeto tipo JSON";
				}
	            return jsonString;

	    }
	
	private Pokemon buildPokemonForPokedex (JSONObject pokemonJson) throws JSONException, IOException {
		Pokemon pokemon = new Pokemon();
		ArrayList<Ability> abilities = new ArrayList<Ability>();
		ArrayList<String> types = new ArrayList<String>();
		URL url;
		StringBuffer response = new StringBuffer();
		
            url = new URL(pokemonJson.getString("url"));
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			http.setRequestProperty("Accept", "application/json");
			int responseCode = http.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						http.getInputStream()));
				String inputLine;
				

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				
				// print result
			} else {
				System.out.println("GET request not worked");
			}
			http.disconnect();

		JSONObject json = new JSONObject(response.toString());
		JSONArray arrayJsonAbilities = json.getJSONArray("abilities");
		for(int i = 0; i< arrayJsonAbilities.length();i++) {
			JSONObject abilitiesJson = arrayJsonAbilities.getJSONObject(i);
			abilities.add(new Ability(abilitiesJson.getJSONObject("ability").getString("name"),abilitiesJson.getBoolean("is_hidden")));
		}
		JSONArray arrayJsonTypes = json.getJSONArray("types");
		for(int i = 0; i< arrayJsonTypes.length();i++) {
			JSONObject typesJson = arrayJsonTypes.getJSONObject(i);
			types.add(typesJson.getJSONObject("type").getString("name"));
		}
		pokemon.setSprite(json.getJSONObject("sprites").getString("front_default"));
		pokemon.setName(pokemonJson.getString("name"));
		pokemon.setTypes(types);
		pokemon.setWeight(json.getString("weight"));
		pokemon.setId(json.getInt("id"));
		pokemon.setAbilities(abilities);
		
		return pokemon;
	}
	@Cacheable(cacheNames = "Pokemon", key="#pokeId", condition = "#pokeId == null")
	@GetMapping(value = "/getSpecifics")
	 public String getSpecifics (HttpSession session, Model model, @RequestParam(value = "pokemonId", required = true) String pokeId) {
				Pokemon pokemonToAdd = new Pokemon();
	           /* if (StringUtils.isBlank(pokeId)) {
	            	page = "0";
	            	//add error
	            }else{
	            	page = pokeId;
	                }*/
				try {
					pokemonToAdd = buildPokemonFromId(Integer.valueOf(pokeId));
				}catch (Exception e) {
					System.out.print(e.getMessage());
					return "Hubo un error consiguiendo la informacion del pokemon especificado";
				}
	            ObjectMapper mapper = new ObjectMapper();
	            //Converting the Object to JSONString
	            String jsonString = "";
				try {
					jsonString = mapper.writeValueAsString(pokemonToAdd);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "Hubo un error mapeando el objeto tipo Json";
				}
	            return jsonString;

	    }
	private Pokemon buildPokemonFromId (Integer pokeId) throws JSONException, IOException {
		PokemonSpecifics pokemon = new PokemonSpecifics();
		ArrayList<Ability> abilities = new ArrayList<Ability>();
		ArrayList<String> types = new ArrayList<String>();
		StringBuffer response = new StringBuffer();
		ArrayList<String> speciesEvolution = new ArrayList<String>();
			response = apiCall(API_URL+"/pokemon/"+String.valueOf(pokeId));
		
		JSONObject json = new JSONObject(response.toString());
		JSONArray arrayJsonAbilities = json.getJSONArray("abilities");
		for(int i = 0; i< arrayJsonAbilities.length();i++) {
			JSONObject abilitiesJson = arrayJsonAbilities.getJSONObject(i);
			abilities.add(new Ability(abilitiesJson.getJSONObject("ability").getString("name"),abilitiesJson.getBoolean("is_hidden")));
		}
		JSONArray arrayJsonTypes = json.getJSONArray("types");
		for(int i = 0; i< arrayJsonTypes.length();i++) {
			JSONObject typesJson = arrayJsonTypes.getJSONObject(i);
			types.add(typesJson.getJSONObject("type").getString("name"));
		}
		
		pokemon.setSprite(json.getJSONObject("sprites").getString("front_default"));
		pokemon.setName(json.getString("name"));
		pokemon.setTypes(types);
		pokemon.setWeight(json.getString("weight"));
		pokemon.setId(json.getInt("id"));
		pokemon.setAbilities(abilities);
		pokemon.setDescription("");
		response = apiCall(API_URL+"/pokemon-species/"+String.valueOf(pokeId));
		
		json = new JSONObject(response.toString());
		JSONArray descriptions = json.getJSONArray("flavor_text_entries");
		for(int i =0;i<descriptions.length();i++) {
			JSONObject description = descriptions.getJSONObject(i);
			if(description.getJSONObject("language").getString("name").equals("en")) {
				pokemon.setDescription(description.getString("flavor_text").replace("", " ").replace("\n", " "));
				break;
			}
		}
		String evolutionChainURL = json.getJSONObject("evolution_chain").getString("url");
			response = apiCall(evolutionChainURL);
		json = new JSONObject(response.toString());
		speciesEvolution.add(json.getJSONObject("chain").getJSONObject("species").getString("name"));
		JSONArray arrayJsonEvolution = json.getJSONObject("chain").getJSONArray("evolves_to");
		if(arrayJsonEvolution.length()>1) {
			for(int i =0;i<arrayJsonEvolution.length();i++) {
				speciesEvolution.add(arrayJsonEvolution.getJSONObject(i).getJSONObject("species").getString("name"));
			}
		}else{
			while(arrayJsonEvolution.length()>0) {
				speciesEvolution.add(arrayJsonEvolution.getJSONObject(0).getJSONObject("species").getString("name"));
				arrayJsonEvolution = arrayJsonEvolution.getJSONObject(0).getJSONArray("evolves_to");
			}
		}
		
		ArrayList<EvolucionChain> evolutionChainArray = new ArrayList<>();
		for(String speciesName : speciesEvolution) {

				response = apiCall(API_URL+"/pokemon/"+speciesName);

			json = new JSONObject(response.toString());
			evolutionChainArray.add(new EvolucionChain(json.getInt("id"),json.getString("name"),json.getJSONObject("sprites").getString("front_default")));

		}
		pokemon.setEvolucionChain(evolutionChainArray);
		
		return pokemon;
		
	}
	private StringBuffer apiCall(String urlApi) throws IOException {
		URL url = new URL(urlApi);
		StringBuffer response = new StringBuffer();
       
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			http.setRequestProperty("Accept", "application/json");
			int responseCode = http.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						http.getInputStream()));
				String inputLine;
				

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				
				// print result
			} else {
				System.out.println("GET request not worked");
			}
			http.disconnect();
			return response;

	}
}
