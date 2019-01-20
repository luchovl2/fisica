package com.luciano.fisica.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.luciano.fisica.entities.Bloque;
import com.luciano.fisica.entities.Polea;
import com.luciano.fisica.entities.Rampa;
import com.luciano.fisica.entities.Rueda;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class FileLoader
{
    //busca un archivo con un nombre y extensión específicos ("scene.txt")
    //lo recorre por línea, buscando que empiecen con alguna de las palabras clave
    //cuando encuentra una, lee las propiedades y crea el objeto que corresponda
    static private Array<Bloque> bloques = null;
    static private Array<Rueda> ruedas = null;
    static private Array<Rampa> rampas = null;
    static private Array<Polea> poleas = null;

    static public void loadSceneFile(World world){
        Path path = Paths.get("scene.txt");

        if(Files.exists(path))
        {
            Gdx.app.log("ARCHIVO", "el archivo existe");

            if(Files.isReadable(path))
            {
                BufferedReader reader = null;
                Charset charset = Charset.forName("ISO-8859-1");

                try
                {
                    reader = Files.newBufferedReader(path, charset);

                    String line = null;
                    Pattern bloquePattern = Pattern.compile("^(bloque)");
                    Pattern ruedaPattern = Pattern.compile("^(rueda)");
                    Pattern rampaPattern = Pattern.compile("^(rampa)");
                    Pattern poleaPattern = Pattern.compile("^(polea)");

                    while((line = reader.readLine()) != null)
                    {
                        //para cada linea, ver si comienza con palabra clave
                        //si lo hace, llamar al método correspondiente
                        if(bloquePattern.matcher(line).find())
                        {
                            Gdx.app.log("ARCHIVO", "linea con bloque");
                            loadBloque(line, world);
                        }
                        else if(ruedaPattern.matcher(line).find())
                        {
                            Gdx.app.log("ARCHIVO", "linea con rueda");
                            loadRueda(line, world);
                        }
                        else if(rampaPattern.matcher(line).find())
                        {
                            Gdx.app.log("ARCHIVO", "linea con rampa");
                            loadRampa(line, world);
                        }
                        else if(poleaPattern.matcher(line).find())
                        {
                            Gdx.app.log("ARCHIVO", "linea con polea");
                            loadPolea(line, world);
                        }
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if(reader != null)
                    {
                        try
                        {
                            reader.close();
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else
            {
                Gdx.app.log("ARCHIVO", "no se puede leer el archivo");
            }
        }
    }

    static private void loadBloque(String line, World world)
    {
        Bloque bloque;
        float masa = 1;
        Vector2 pos = new Vector2(1f, 1f);
        float width = 0.2f;
        float height = 0.1f;
        float angle = 0f;

        //buscar los atributos: -masa -posx -posy -ancho -alto
        String[] words = line.replace(',', '.').split(" ");

        for(int i=0; i<words.length-1; i++)
        {
            if(words[i].equals("-masa"))
            {
                masa = Float.parseFloat(words[i+1]);
            }
            else if(words[i].equals("-posx"))
            {
                pos.set(Float.parseFloat(words[i+1]), pos.y);
            }
            else if(words[i].equals("-posy"))
            {
                pos.set(pos.x, Float.parseFloat(words[i+1]));
            }
            else if(words[i].equals("-ancho"))
            {
                width = Float.parseFloat(words[i+1]);
            }
            else if(words[i].equals("-alto"))
            {
                height = Float.parseFloat(words[i+1]);
            }
        }

        bloque = new Bloque(world, pos, width, height, angle);
        bloque.changeMass(masa);

        if(bloques == null)
        {
            bloques = new Array<Bloque>();
        }
        bloques.add(bloque);
    }

    static private void loadRueda(String line, World world)
    {
        Rueda rueda;
        float masa = 1;
        Vector2 pos = new Vector2(2f, 1f);
        float radius = 0.2f;

        //buscar los atributos: -masa -posx -posy -ancho -alto
        String[] words = line.replace(',', '.').split(" ");

        for(int i=0; i<words.length-1; i++)
        {
            if(words[i].equals("-masa"))
            {
                masa = Float.parseFloat(words[i+1]);
            }
            else if(words[i].equals("-posx"))
            {
                pos.set(Float.parseFloat(words[i+1]), pos.y);
            }
            else if(words[i].equals("-posy"))
            {
                pos.set(pos.x, Float.parseFloat(words[i+1]));
            }
            else if(words[i].equals("-radio"))
            {
                radius = Float.parseFloat(words[i+1]);
            }
        }

        rueda = new Rueda(world, pos, radius);
        rueda.changeMass(masa);

        if(ruedas == null)
        {
            ruedas = new Array<Rueda>();
        }
        ruedas.add(rueda);
    }

    static private void loadPolea(String line, World world)
    {
        //Polea polea = new Polea(world, new Vector2(2f, 3f));
    }

    static private void loadRampa(String line, World world)
    {
        Rampa rampa;
        Vector2 pos = new Vector2(3f, 1f);
        float ancho = 1f;
        float alto = 1f;
        float pendiente = 1f;

        //buscar los atributos: -posx -posy -ancho -alto
        String[] words = line.replace(',', '.').split(" ");

        for(int i=0; i<words.length-1; i++)
        {
            if(words[i].equals("-posx"))
            {
                pos.set(Float.parseFloat(words[i+1]), pos.y);
            }
            else if(words[i].equals("-posy"))
            {
                pos.set(pos.x, Float.parseFloat(words[i+1]));
            }
            else if(words[i].equals("-ancho"))
            {
                ancho = Float.parseFloat(words[i+1]);
            }
            else if(words[i].equals("-alto"))
            {
                alto = Float.parseFloat(words[i+1]);
            }
        }

        rampa = new Rampa(world, pos, ancho, alto);

        if(rampas == null)
        {
            rampas = new Array<Rampa>();
        }
        rampas.add(rampa);
    }

    static public Array<Rampa> getRampas(){
        return rampas;
    }

    static public Array<Bloque> getBloques(){
        return bloques;
    }

    static public Array<Rueda> getRuedas(){
        return ruedas;
    }
}
