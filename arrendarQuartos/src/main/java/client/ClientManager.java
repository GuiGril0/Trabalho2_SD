/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

/**
 *
 * @author gui
 */
public class ClientManager {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static String SERVER_URL;

    private static void getBaseURL() throws Exception{
        InputStream is = new FileInputStream("src/main/resources/configs.properties");
        Properties p = new Properties();
        p.load(is);
        SERVER_URL = p.getProperty("baseuri") + "server";
    }

    private static JSONArray convertJsonStringToJsonArray(String json) {
        return new JSONArray(json);
    }

    /*
    função responsável por mostrar o menu
     */
    private static void showMenu() {
        try {

            System.out.println("1 - Mostrar anúncios por estado (ativo e inativo)");
            System.out.println("2 - Alterar o estado de um anúncio (inserir aid do anúncio)");
            System.out.println("0 - Sair");

            int option = -1;
            try {
                System.out.print("Opção: ");
                option = Integer.parseInt(br.readLine());
                if (option < 0 || option > 2) {
                    System.out.println("Insira uma das opções apresentadas!");
                    showMenu();
                }
            } catch (NumberFormatException e) {
                System.err.println("Opção inválida!");
                showMenu();
            }
            chooseOption(option);
        } catch(IOException e) {
            System.err.println("Argumentos inválidos!");
            showMenu();
        }
    }

    /*
    função que recebe a opção escolhida pelo admin e
    encaminha o admin para a função que execute
    o requisitado
     */
    private static void chooseOption(int option) throws IOException {
        switch(option) {
            case 0:
                System.out.println("Adeus admin! Até à próxima!");
                System.exit(1);
                break;
            case 1:
                showAdsByState("ativo");
                showAdsByState("inativo");
                break;
            case 2:
                getAdByAid();
                break;
            default:
                showMenu();
        }
        showMenu();
    }

    private static void showAdsByState(String state) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL + "/ads?state=" + state).openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);

            BufferedReader b = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line = "";
            while((line = b.readLine()) != null)
                response.append(line);
            b.close();

            JSONArray jsonArray = convertJsonStringToJsonArray(response.toString());

            if(jsonArray.length() == 0)
                System.out.println("Nenhum anúncio no estado " + state + " encontrado!\n");
            else {
                System.out.println("    ==>Anúncios no estado " + state + "<==\n");
                for(int i=0; i<jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    System.out.println("  Anúncio " + jsonObject.get("aid"));
                    for(Iterator it = jsonObject.keys(); it.hasNext();) {
                        String key = it.next().toString();
                        if (key.equals("date"))
                            System.out.println(key + ": " + jsonObject.getString(key).substring(0, jsonObject.getString(key).length() - 1));
                        else
                            System.out.println(key + ": " + jsonObject.get(key));
                    }
                    System.out.println();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void getAdByAid() {
        try {
            int aid = -1;
            do {
                try {
                    System.out.print("Insira o aid: ");
                    aid = Integer.parseInt(br.readLine());
                    if(aid > 0)
                        break;
                    System.err.println("Insira um número inteiro positivo!");
                } catch(NumberFormatException e) {
                    System.err.println("Formato inválido! Insira um número inteiro positivo!");
                }
            } while(true);

            HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL + "/ads?aid=" + aid).openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);

            BufferedReader b = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line = "";
            while((line = b.readLine()) != null)
                response.append(line);
            b.close();

            JSONArray jsonArray = convertJsonStringToJsonArray(response.toString());

            if(jsonArray.length() == 0)
                System.out.println("Nenhum anúncio encontrado com aid " + aid);
            else {
                System.out.println();
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                System.out.println("  Anúncio " + jsonObject.get("aid"));
                for (Iterator it = jsonObject.keys(); it.hasNext(); ) {
                    String key = it.next().toString();
                    if (key.equals("date"))
                        System.out.println(key + ": " + jsonObject.getString(key).substring(0, jsonObject.getString(key).length() - 1));
                    else
                        System.out.println(key + ": " + jsonObject.get(key));
                }
                System.out.println();

                String choice = "";
                do {
                    System.out.print("Deseja alterar o anúncio com aid " + aid + ": ");
                    choice = br.readLine();
                    choice = choice.trim();
                    choice = choice.toLowerCase();
                    if(choice.equals("s") || choice.equals("sim") || choice.equals("n") || choice.equals("nao") || choice.equals("não"))
                        break;
                    System.err.println("Insira s/sim ou n/nao/não!");
                } while(true);

                if(choice.equals("s") || choice.equals("sim")) {
                    if(changeAdState(aid))
                        System.out.println("Ok estado do anúncio com aid " + aid + " alterado com sucesso!");
                    else
                        System.err.println("Erro ao alterar estado do anúncio com aid " + aid);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean changeAdState(int aid) {
        try {
            String state = "";
            do {
                System.out.print("Insira o estado: ");
                state = br.readLine();
                state = state.trim();
                state = state.toLowerCase();
                if(state.equals("a") || state.equals("ativo") || state.equals("i") || state.equals("inativo"))
                    break;
                System.err.println("Formato inválido!");
            } while(true);

            if(state.equals("a"))
                state = "ativo";
            else if(state.equals("i"))
                state = "inativo";

            HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL + "/changead?state=" + state + "&aid=" + Integer.toString(aid)).openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            BufferedReader b = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = b.readLine();
            b.close();

            return response.equals("true");
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void main(String[] args) {
        System.out.println("\n Bem vindo ao sistema de oferta e procura de alojamentos!");

        try {
            getBaseURL();
            showMenu();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}
