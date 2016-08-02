import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.BufferedReader;
import java.util.Scanner;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;
import javax.net.ssl.*;

public class downloader {
    public static void main(String[] args) throws IOException {
        System.out.println("youtube playlist url parser");
        System.out.println("enter the url of the playlist");

        //instantiate a bufferedreader andstring tokenizer, which will read the playlist url
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(in.readLine());
        //read the playlist url
        URL url = new URL(st.nextToken());
        //open the connection to the url
        URLConnection connection = url.openConnection();
        //start the inputstream
        InputStream is = connection.getInputStream();
        //connect the inputstream to the reader
        InputStreamReader isr = new InputStreamReader(is);
        //create buffered reader with the isr
        BufferedReader reader = new BufferedReader(isr);
        
        ArrayList<Music> songs = new ArrayList<Music>(); 
        String line;
        while((line = reader.readLine()) != null) {
            if(line.contains("<tr") && line.contains("id=")) {
                //System.out.println(line);

                String id = line.substring(line.indexOf("data-video-id=")+15,line.indexOf("data-video-id=")+15+11);
                String title = line.substring(line.indexOf("title=")+7,line.indexOf("\"",line.indexOf("title=")+7));
                //System.out.println(title);
                songs.add(new Music(new URL("https://www.youtubeinmp3.com/fetch/?video=https://www.youtube.com/watch?v="+id),title));
                System.out.println("https://www.youtube.com/watch?v="+id);
            }
        }
       //NOT MY TrustManager CODE!!!!
       // Create a new trust manager that trust all certificates
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        // Activate the new trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        } 
        

        ///// my code begins again
        for(Music each : songs) {
            URLConnection downloadConnection = each.url.openConnection();
            InputStream data = downloadConnection.getInputStream();

            OutputStream output;
            try {
                output = new FileOutputStream(new File("music/"+each.title+".mp3"));
                byte[] buffer = new byte[4096];
                int len;
                while((len = data.read(buffer)) > 0) {
                    output.write(buffer,0,len);
                }
                output.close();

            }
            catch(Exception exc) {
                System.out.println("could not create file (or something else went wrong) for the song: "+each.title);
            }
            
        }
        
    }
}
