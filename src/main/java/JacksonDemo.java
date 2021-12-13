import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class JacksonDemo {

    //User för demo.
    User userObject = new User("Kalle", "Anka", "kalle@email.com", "03029320",
            new Adress("Paradisvägen 111", "0153", "Ankeborg"));

    //Fil att generera/ läsa ifrån.
    final File JSON_FILE = new File("user.json");
    //Återanvänd Objectmapper i alla metoder! Kostsam att instansera!
    final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        JacksonDemo demo = new JacksonDemo();
        //demo.javaObjectToJsonFile();
        //demo.javaObjectToJsonOutPut();
        //demo.javaObjectToJsonString();
        demo.jsonFileToJavaObject();
        //demo.jsonFileToJsonNode();
        //demo.parseJsonFileThroughStreaming();
        //demo.jsonGenerator();

    }

    public void javaObjectToJsonFile() throws IOException{
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        writer.writeValue(JSON_FILE, userObject);
    }

    public void javaObjectToJsonOutPut() throws IOException{
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        writer.writeValue(System.out, userObject);
    }

    public void javaObjectToJsonString() throws JsonProcessingException {
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        String jsonString = writer.writeValueAsString(userObject);
        System.out.println("JSON STRING:" + jsonString);
    }

    //Skapar en user från user.json
    //Behöver to string metod i User och Adress för att vi ska kunna se resultat i sout
    //Om man lägger till ett fält i bara user.json så blir det error, behövs @JsonIgnoreProperties(ignoreUnknown = true)
    // i user klassen för att den ska ignorera okända fält och kunna skapas.
    //Man kan också lägga till annotationen @JsonProperty("phone")
    // ifall fältnament ändras i klassen. Tex phone till phoneNumber.
    public void jsonFileToJavaObject() throws IOException {
        User userParsedFromJsonFile = mapper.readValue(JSON_FILE, User.class);
        System.out.println("JAVA OBJECT PARSED FROM FILE: " + userParsedFromJsonFile);
    }
    public void javaObjecToJsonWithViews()throws IOException{
        //Format Json
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        ObjectWriter writer = mapper.writerWithView(JsonViews.Normal.class);
        String jsonString = writer.writeValueAsString(userObject);

        //Alternativ syntax utan att exclipt instansera objectWriter

        //String jsonString = mapper.writerWithView(JsonViews.Admin.class).writeValueAsString(userObject);
        System.out.println("Json string with views: " + jsonString);
    }

    public void jsonFileToJsonNode() throws IOException{
        JsonNode rootNode = mapper.readTree(JSON_FILE);

        Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();

        while (fields.hasNext()){
            Map.Entry<String,JsonNode> field = fields.next();
            String fieldName = field.getKey();
            JsonNode childNode = field.getValue();
            System.out.println("Key" + fieldName);
            System.out.println("Value " + childNode);
        }

        //Hämta värdet från rootnode
        String phoneNumber = rootNode.get("phone").asText();
        System.out.println("PhoneNumber: "+phoneNumber);

        //Hämta värde från subNode
        JsonNode adressNode = rootNode.path("adress");
        String city = adressNode.get("city").asText();
        System.out.println("City: " + city);
    }

    public void parseJsonFileThroughStreaming() throws IOException{
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(JSON_FILE);
        JsonToken token;

        while ((token = parser.nextToken()) != null){
/*            System.out.println("Token name: " + token);
            System.out.println("From parser: " + parser.getText());*/

            if(token.isScalarValue()){
                String currentName = parser.currentName();
                if(currentName != null){
                    String text = parser.getText();
                    System.out.println(currentName+ " : " + text);
                }
            }
        }
    }

    public void jsonGenerator () throws IOException{
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());

        //"Förnamn": "kalle",
        jsonGenerator.writeStartObject();
        //"efterNamn": "Anka"
        jsonGenerator.writeStringField("förnamn: " , userObject.getFirstName());
        jsonGenerator.writeStringField("efternamn: " , userObject.getLastName());
        //{}
        jsonGenerator.writeEndObject();

        //Skriver ut
        jsonGenerator.flush();
        //avslutar
        jsonGenerator.close();
    }

}
