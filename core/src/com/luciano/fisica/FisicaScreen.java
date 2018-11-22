package com.luciano.fisica;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PulleyJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.luciano.fisica.entities.Bloque;
import com.luciano.fisica.entities.Pared;
import com.luciano.fisica.entities.Rueda;
import com.luciano.fisica.utils.Constants;

import static com.luciano.fisica.utils.Constants.PPM;

public class FisicaScreen extends ScreenAdapter
    implements Input.TextInputListener, InputProcessor
{
    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private Viewport viewport;

    private Box2DDebugRenderer debugRenderer;
    private World world;

    private Array<Pared> contorno;

    public Array<FisicaBody> cuerpos;
    public Array<FisicaBody> obstaculos;

    private Array<Bloque> bloques;
    private Array<Rueda> ruedas;

    private boolean pause;
    private int cuerpoActual = -1;
    private int obstaculoActual = -1;

    private FisicaUI fisicaUI;

    public FisicaScreen()
    {
        //fisicaUI = new FisicaUI();
        //fisicaUI.init();
    }

    @Override
    public void show()
    {
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();

        Gdx.input.setInputProcessor(this);

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        viewport = new FitViewport(Gdx.graphics.getWidth()/PPM, Gdx.graphics.getHeight()/PPM);

        world = new World(new Vector2(0f, -9.81f), false);
        debugRenderer = new Box2DDebugRenderer();

        pause = true;

        cuerpos = new Array<FisicaBody>();
        obstaculos = new Array<FisicaBody>();

        contorno = new Array<Pared>(4);
        crearContorno(contorno, width, height);

        bloques = new Array<Bloque>();
        ruedas = new Array<Rueda>();

        ruedas.add(new Rueda(world, new Vector2(1, 1), 0.4f));
        bloques.add(new Bloque(world, new Vector2(4, 2), 1f, 0.6f, 0f));
    }

    @Override
    public void render(float delta)
    {
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        for(Pared pared:contorno)
            pared.render(batch);

        for(Bloque bloque:bloques)
            bloque.render(batch);

        for(Rueda rueda:ruedas)
            rueda.render(batch);

        batch.end();

        debugRenderer.render(world, viewport.getCamera().combined);

        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        for(FisicaBody cuerpo:cuerpos)
            cuerpo.render(renderer);

        for(FisicaBody obstaculo:obstaculos)
            obstaculo.render(renderer);

        renderer.end();

    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height, true);
    }

    public void update(float delta)
    {
        if(!pause)
        {
            world.step(1 / 60f, 6, 2);
        }

        inputUpdate(delta);
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        renderer.dispose();
        debugRenderer.dispose();
        world.dispose();
    }

    public void inputUpdate(float delta)
    {
        Vector2 force = new Vector2(0, 0);

        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
        {
            //bloques.get(0).body.applyForceToCenter(-20, 0, false);
            ruedas.get(0).body.applyForceToCenter(-20, 0, false);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
        {
            //bloques.get(0).body.applyForceToCenter(20, 0, false);
            ruedas.get(0).body.applyForceToCenter(20, 0, false);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
        {
            //bloques.get(0).body.applyForceToCenter(0, 60, false);
            ruedas.get(0).body.applyForceToCenter(0, 30, false);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            force.x -= 1;

            if(0 <= obstaculoActual && obstaculoActual < obstaculos.size)
            {
                Vector2 pos = obstaculos.get(obstaculoActual).body.getPosition();
                pos.x -= 0.02;
                obstaculos.get(obstaculoActual).body.setTransform(pos, 0);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            force.x += 1;
            if(0 <= obstaculoActual && obstaculoActual < obstaculos.size)
            {
                Vector2 pos = obstaculos.get(obstaculoActual).body.getPosition();
                pos.x += 0.02;
                obstaculos.get(obstaculoActual).body.setTransform(pos, 0);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            if(0 <= obstaculoActual && obstaculoActual < obstaculos.size)
            {
                Vector2 pos = obstaculos.get(obstaculoActual).body.getPosition();
                pos.y += 0.02;
                obstaculos.get(obstaculoActual).body.setTransform(pos, 0);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            if(0 <= obstaculoActual && obstaculoActual < obstaculos.size)
            {
                Vector2 pos = obstaculos.get(obstaculoActual).body.getPosition();
                pos.y -= 0.02;
                obstaculos.get(obstaculoActual).body.setTransform(pos, 0);
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
        {
            force.y = 15;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
        {
            force.y = -15;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.P))
        {
            pause = !pause;
        }

        if(0 <= cuerpoActual && cuerpoActual < cuerpos.size)
        {
            cuerpos.get(cuerpoActual).body.applyForceToCenter(force, false);
        }
    }

    public void agregarCuerpo(float posX, float posY, float width, float height, Formas forma)
    {
/*        FisicaBody body = new FisicaBody();
        body.width = width;
        body.height = height;
        body.color = Color.FIREBRICK;
        body.forma = forma;
        body.body = null;

        if(forma == Formas.CAJA)
            body.body = createBodyBox(posX, posY, body.width, body.height, 1, false);
        else if(forma == Formas.BOLA)
            body.body = createBodyBall(posX, posY, body.width, false);

        cuerpos.add(body);
  */  }

    public void agregarObstaculo(float posX, float posY, float width, float height, float roza, Formas forma)
    {
    /*    FisicaBody body = new FisicaBody();
        body.width = width;
        body.height = height;
        body.color = Color.DARK_GRAY;
        body.forma = forma;
        body.body = null;

        if(forma == Formas.VIGA)
            body.body = createBodyBox(posX, posY, body.width, body.height, roza, true);
        else if(forma == Formas.RAMPA)
            body.body = createBodyRamp(posX, posY, body.width, body.height, roza, true);

        obstaculos.add(body);*/
    }

    public void agregarPolea(Body bodyA, Body bodyB, Body groundA, Body groundB)
    {
        PulleyJointDef jointDef = new PulleyJointDef();
        jointDef.initialize(bodyA, bodyB,
                groundA.getWorldCenter(),
                groundB.getWorldCenter(),
                bodyA.getWorldCenter(),
                bodyB.getWorldCenter(),
                1);
        jointDef.lengthA = 0.5f + groundA.getPosition().dst(jointDef.groundAnchorB);
        jointDef.lengthB = 0.4f;
        world.createJoint(jointDef);
    }

    private void crearContorno(Array<Pared>contorno, float width, float height)
    {
        //piso
        contorno.add(new Pared(world,
                new Vector2(width/2/PPM, 0),
                width/PPM,
                Constants.PARED_GROSOR));
        //derecha
        contorno.add(new Pared(world,
                new Vector2(width/PPM, height/2/PPM),
                Constants.PARED_GROSOR,
                height/PPM));
        //techo
        contorno.add(new Pared(world,
                new Vector2(width/2/PPM, height/PPM),
                width/PPM,
                Constants.PARED_GROSOR));
        //izquierda
        contorno.add(new Pared(world,
                new Vector2(0, height/2/PPM),
                Constants.PARED_GROSOR,
                height/PPM));
    }

    @Override
    public void input(String text)
    {
        //if(text != null && text.contains("masa player = "))
        if(0 <= cuerpoActual && cuerpoActual < cuerpos.size)
        {
            String numero = text.substring(text.indexOf("=")+1, text.length());
            try
            {
                float valor = Float.valueOf(numero.trim());
                Gdx.app.log("Nueva masa: ", String.valueOf(valor));

                MassData massData = cuerpos.get(cuerpoActual).body.getMassData();
                massData.mass = valor;
                cuerpos.get(cuerpoActual).body.setMassData(massData);
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void canceled(){}

    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        boolean retorno = false;

        if(button == Input.Buttons.LEFT)
        {
            retorno = true;

            Vector3 punto = viewport.getCamera().unproject(new Vector3(screenX, screenY, 0));
            Array<Fixture> fixtures;

            cuerpoActual = -1;
            obstaculoActual = -1;

            for(FisicaBody cuerpo:cuerpos)
            {
                fixtures = cuerpo.body.getFixtureList();

                for (Fixture fix : fixtures)
                {
                    if (fix.testPoint(punto.x/PPM, punto.y/PPM))
                    {
                        int indice = cuerpos.indexOf(cuerpo, false);
                        cuerpoActual = indice;

                        Gdx.app.log("Tu vieja", "cuerpo " + indice + " clickeado: " +
                                punto.toString());
                        MassData massData = cuerpo.body.getMassData();
                        Gdx.input.getTextInput(this, "Cuerpo " + indice,
                                "masa cuerpo " + indice + " = " + massData.mass, "");
                    }
                }
            }

            for(FisicaBody obstaculo:obstaculos)
            {
                fixtures = obstaculo.body.getFixtureList();

                for(Fixture fix:fixtures)
                {
                    if(fix.testPoint(punto.x/PPM, punto.y/PPM))
                    {
                        int indice = obstaculos.indexOf(obstaculo, false);
                        obstaculoActual = indice;
                    }
                }
            }
        }
        return retorno;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }

    enum Formas
    {
        CAJA,
        BOLA,
        RAMPA,
        VIGA,
        POLEA
    }
}
