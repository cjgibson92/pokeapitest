package com.pokeapi2.project2.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pokeapi2.project2.entities.Ability;
import com.pokeapi2.project2.entities.Pokemon;

@Controller
public class PokeApiController {

	 public static final String API_URL = "https://pokeapi.co/api/v2/";
	
	@RequestMapping("/")
	public String home() {
		System.out.print("going home");
	
			
	
		return "index";
	}
	@RequestMapping(value = "/getPokedex", method = RequestMethod.GET)
	 public String GetPokedex (HttpSession session, Model model, @RequestParam(value = "offset", required = false) String offset) {
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
	            url = new URL(API_URL+"pokemon?limit=20&offset="+page);
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
			}	
	            
	            model.addAttribute("pokemonList",pokemonList);
	            return "pokedex";

	    }
	
	private Pokemon buildPokemonForPokedex (JSONObject pokemonJson) throws JSONException {
		Pokemon pokemon = new Pokemon();
		ArrayList<Ability> abilities = new ArrayList<Ability>();
		ArrayList<String> types = new ArrayList<String>();
		URL url;
		StringBuffer response = new StringBuffer();
		try {
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	@RequestMapping(value = "/getSpecifics", method = RequestMethod.GET)
	 public String getSpecifics (HttpSession session, Model model, @RequestParam(value = "pokemonId", required = true) String pokeId) {
				ArrayList<Pokemon> pokemonList = new ArrayList<>();
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
				}
				
	            model.addAttribute("pokemon",pokemonToAdd);
	            return "pokemonSpecific";

	    }
	private Pokemon buildPokemonFromId (Integer pokeId) throws JSONException {
		Pokemon pokemon = new Pokemon();
		ArrayList<Ability> abilities = new ArrayList<Ability>();
		ArrayList<String> types = new ArrayList<String>();
		StringBuffer response = new StringBuffer();
		ArrayList<String> speciesEvolution = new ArrayList<String>();
		try {
			response = apiCall(API_URL+"/pokemon/"+String.valueOf(pokeId));
		}catch (Exception e) {
			
		}
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
		try {
			response = apiCall(API_URL+"/pokemon-species/"+String.valueOf(pokeId));
		}catch (Exception e) {
			
		}
		json = new JSONObject(response.toString());
		JSONArray descriptions = json.getJSONArray("flavor_text_entries");
		for(int i =0;i<descriptions.length();i++) {
			JSONObject description = descriptions.getJSONObject(i);
			if(description.getJSONObject("language").getString("name").equals("en")) {
				pokemon.setDescription(description.getString("flavor_text").replace("", " "));
				break;
			}
		}
		String evolutionChainURL = json.getJSONObject("evolution_chain").getString("url");
		try {
			response = apiCall(evolutionChainURL);
		}catch (Exception e) {
			
		}
		json = new JSONObject(response.toString());
		speciesEvolution.add(json.getJSONObject("chain").getJSONObject("species").getString("url"));
		JSONArray arrayJsonEvolution = json.getJSONObject("chain").getJSONArray("evolves_to");
		while(arrayJsonEvolution.length()>0) {
			speciesEvolution.add(arrayJsonEvolution.getJSONObject(0).getJSONObject("species").getString("url"));
			arrayJsonEvolution = arrayJsonEvolution.getJSONObject(0).getJSONArray("evolves_to");
		}
		Pokemon pokemonChain = new Pokemon();
		ArrayList<Pokemon> evolutionChainArray = new ArrayList<>();
		for(String speciesURL : speciesEvolution) {
			pokemonChain = new Pokemon();
			try {
				response = apiCall(speciesURL);
				
				
			}catch (Exception e) {
				
			}
			json = new JSONObject(response.toString());
		
			pokemonChain.setId(json.getInt("id"));
			pokemonChain.setName(json.getString("name"));
			evolutionChainArray.add(pokemonChain);
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
