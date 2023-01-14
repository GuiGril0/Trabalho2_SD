/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sd.client;
import org.json.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.*;

import sd.rest.Ad;
import sd.rest.Message;

/**
 *
 * @author gui
 */
public class Client {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static String SERVER_URL;

    private static void getBaseURL() throws Exception{
        InputStream is = new FileInputStream("src/main/resources/configs.properties");
        Properties p = new Properties();
        p.load(is);
        SERVER_URL = p.getProperty("baseuri") + "/server";
    }

    private static JSONArray convertJsonStringToJsonArray(String json) {
        return new JSONArray(json);
    }

    /*
    função responsável por mostrar o menu para
    escolher a funcionalidade desejada
     */
    private static void showMenu() {
        try {
            System.out.println("1 - Registar um novo anúncio");
            System.out.println("2 - Mostrar anúncios (oferta ou procura)");
            System.out.println("3 - Mostrar todos os anúncios por descrição e, opcionalmente, também por localização");
            System.out.println("4 - Obter detalhes de um anúncio (inserir aid do anúncio)");
            System.out.println("5 - Enviar mensagem para um anúncio (inserir aid do anúncio)");
            System.out.println("6 - Consultar mensagens de um anúncio (inserir aid do anúncio)");
            System.out.println("0 - Sair");

            int option = -1;
            try {
                System.out.print("Opção: ");
                option = Integer.parseInt(br.readLine());
                if (option < 0 || option > 6) {
                    System.out.println("Insira uma das opções apresentadas!");
                    showMenu();
                }
            } catch(NumberFormatException e) {
                System.out.println("Opção inválida!");
                showMenu();
            }

            chooseOption(option);
        }
        catch (IOException e) {
            System.err.println("Argumentos inválidos!");
            showMenu();
        }
    }

    /*
    função responsável por selecionar a opção desejada pelo cliente
    */
    private static void chooseOption(int option) throws IOException{
        switch (option) {
            case 0:
                System.out.println("\n## Adeus! Até à próxima!");
                System.exit(1);
                break;
            case 1:
                registerAd();
                break;

            case 2:
                searchAds(0);
                break;

            case 3:
                searchAds(1);
                break;
            case 4:
                searchAds(2);
                break;
            case 5:
                sendMessage();
                break;
            case 6:
                showMessages();
                break;
            default:
                showMenu();
        }
        showMenu();
    }

    private static void registerAd() throws IOException {
        Ad ad = new Ad();

        String type = "";
        do {
            System.out.print("Insira o tipo de anúncio: ");
            type = br.readLine();
            type = type.toLowerCase();
            if(type.equals("oferta") || type.equals("procura"))
                break;
        } while(true);
        ad.setType(type);

        String name = "";
        do {
            System.out.print("Insira o seu nome: ");
            name = br.readLine();
            name = name.trim();
        } while(name.length() == 0);
        ad.setAdvertiser(name);

        String local = "";
        do {
            System.out.print("Insira a localização do alojamento: ");
            local = br.readLine();
            local = local.trim();
        } while(local.length() == 0);
        ad.setLocal(local);

        double price = -1;
        do {
            System.out.print("Insira o preço do alojamento: ");
            try {
                price = Double.parseDouble(br.readLine());
                if(price > 0.0)
                    break;
            } catch(NumberFormatException e) {
                System.err.println("Formato inválido! Insira um número positivo!");
                continue;
            }
        } while(true);
        ad.setPrice(price);

        String gender = "";
        do {
            System.out.print("Insira o género que pretende para potenciais interessados (masculino, feminino ou indiferente): ");
            gender = br.readLine();
            gender = gender.trim();
            gender = gender.toLowerCase();
            if(gender.equals("masculino") || gender.equals("feminino") || gender.equals("indiferente"))
                break;
        } while(true);
        ad.setGender(gender);

        String typology = "";
        int n = -1;
        do {
            System.out.print("Insira a tipologia do alojamento (quarto ou T0, T1...): ");
            typology = br.readLine();
            typology = typology.trim();
            typology = typology.toLowerCase();
            if(typology.equals("quarto"))
                break;
            else if(typology.startsWith("t")){
                String aux = typology.substring(1, typology.length());
                try {
                    n = Integer.parseInt(aux);
                    if(n >= 0) {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Formato errado! TN (sendo N um número inteiro maior ou igual a 0");
                    continue;
                }
            }
            else
                continue;
        } while(true);
        if(typology.startsWith("t"))
            typology = "T" + String.valueOf(n);
        ad.setTypology(typology);

        String description = "";
        while(description.equals("")) {
            System.out.print("Insira uma descrição para o anúncio: ");
            description = br.readLine();
            description = description.trim();
        }
        ad.setDescription(description);

        int aid = postAd(ad);
        ad.setAid(aid);
        if(aid != -1) {
            System.out.println("Ok anúncio " + ad.getAid());

        }
        else
            System.err.println("\n Erro ao postar o anúncio!");
    }

    private static int postAd(Ad ad) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL + "/ad?").openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);


            String jsonObject = "{\"advertiser\":\"" + ad.getAdvertiser()
                    + "\",\"type\":\"" + ad.getType()
                    + "\", \"price\":" + ad.getPrice()
                    + ", \"gender\":\"" + ad.getGender()
                    + "\", \"local\":\"" + ad.getLocal()
                    + "\", \"typology\":\"" + ad.getTypology()
                    + "\", \"description\":\"" + ad.getDescription()
                    + "\"}";

            OutputStream os = connection.getOutputStream();
            os.write(jsonObject.getBytes(StandardCharsets.UTF_8));



            BufferedReader b = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line = "";
            while((line = b.readLine()) != null)
                response.append(line);
            b.close();

           return Integer.parseInt(response.toString().trim());
        } catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static int searchAds(int value) {
        try {
            if (value == 0) {
                String fields = "";
                do {
                    System.out.print("Insira se pretende visualizar anúncios do tipo oferta ou procura: ");
                    fields = br.readLine();
                    if(fields.equals("oferta") || fields.equals("procura"))
                        break;
                    System.out.println("Insira se pretende anúncios do tipo oferta ou do tipo procura!");
                } while(true);
                HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL + "/ads?type=" + fields).openConnection();
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
                    System.out.println("Nenhum anúncio encontrado!");
                else {
                    System.out.println();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        System.out.println("  Anúncio " + jsonObject.get("aid"));
                        System.out.println("typology: " + jsonObject.get("typology") +
                                "\ngender: " + jsonObject.get("gender") +
                                "\nprice: " + jsonObject.get("price") +
                                "\ntype: " + jsonObject.get("type") +
                                "\nlocal: " + jsonObject.get("local"));
                        System.out.println();
                    }
                }
            }
            else if(value == 1) {
                String fields = "";

                String description = "";
                do {
                    System.out.print("Insira a descrição: ");
                    description = br.readLine();
                } while(description.equals(""));
                fields = "&description=" + description;

                String local = "";
                System.out.print("Insira uma localização (se não pretender carregue enter): ");
                local = br.readLine();
                local = local.trim();
                fields += "&local=" + local;

                HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL + "/ads?" + fields).openConnection();
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

                if(jsonArray.length() == 0) {
                    System.out.print("Nenhum anúncio encontrado com a descrição \"" + description + "\"");
                    if(local.length() > 0)
                        System.out.print(" e local \"" + local + "\"");
                    System.out.println("!\n");
                }
                else {
                    System.out.println();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        System.out.println("  Anúncio " + jsonObject.get("aid"));
                        System.out.println("typology: " + jsonObject.get("typology") +
                                "\ngender: " + jsonObject.get("gender") +
                                "\nprice: " + jsonObject.get("price") +
                                "\ntype: " + jsonObject.get("type") +
                                "\nlocal: " + jsonObject.get("local"));
                        System.out.println();
                    }
                }
            }
            else {
                String aid = "";
                do {
                    System.out.print("Insira o aid: ");
                    aid = br.readLine();
                    try {
                        int n = Integer.parseInt(aid);
                        if(n > 0)
                            break;
                        System.err.println("Introduza um número inteiro positivo!");
                    } catch(NumberFormatException e) {
                        System.err.println("Formato errado! Introduza um número inteiro positivo!");
                        continue;
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
                if(jsonArray.length() == 0) {
                    System.out.println("Nenhum anúncio encontrado com aid " + aid);
                    return -1;
                }
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
                    return Integer.parseInt(aid);
                }
            }
            return 0;
        } catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static void sendMessage() throws IOException{
        Message msg = new Message();
        int aid = searchAds(2);
        if(aid > 0) {
            msg.setAid(aid);

            String fields = "";
            do {
                System.out.print("Insira o seu nome: ");
                fields = br.readLine();
                fields = fields.trim();
            } while(fields.equals(""));
            msg.setSender(fields);

            fields = "";
            do {
                System.out.print("Insira o conteúdo da mensagem: ");
                fields = br.readLine();
                fields = fields.trim();
            } while(fields.equals(""));
            msg.setContent(fields);

            if(postMessage(msg))
                System.out.println("Ok mensagem enviada para o anúncio " + aid);
            else
                System.out.println("Erro mensagem não enviada para o anúncio " + aid);
        }
    }

    private static boolean postMessage(Message msg) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL + "/msg").openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String jsonObject = "{\"sender\":\"" + msg.getSender()
                    + "\",\"content\":\"" + msg.getContent()
                    + "\", \"aid\":\"" + msg.getAid()
                    + "\"}";

            OutputStream os = connection.getOutputStream();
            os.write(jsonObject.getBytes(StandardCharsets.UTF_8));

            BufferedReader b = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line = "";
            while((line = b.readLine()) != null)
                response.append(line);
            b.close();

            return response.toString().trim().equals("true");
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void showMessages() {
        int aid = searchAds(2);
        if(aid > 0) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL + "/msgs?aid=" + Integer.toString(aid)).openConnection();
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
                    System.out.println("Nenhuma mensagem encontrada para o anúncio " + aid + "!");
                else {
                    System.out.println("  Mensagens do anúncio " + aid + "\n");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int j = i + 1;
                        System.out.println("Mensagem " + j);
                        for (Iterator it = jsonObject.keys(); it.hasNext(); ) {
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
