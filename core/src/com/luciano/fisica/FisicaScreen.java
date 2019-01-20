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
import com.luciano.fisica.entities.Polea;
import com.luciano.fisica.entities.Rampa;
import com.luciano.fisica.entities.Rueda;
import com.luciano.fisica.utils.Constants;
import com.luciano.fisica.utils.FileLoader;
import com.luciano.fisica.utils.Grid;
import com.luciano.fisica.utils.Hud;

import static com.luciano.fisica.utils.Constants.PPM;

public class FisicaScreen extends ScreenAdapter
    implements InputProcessor
{
    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private Viewport viewport;

    private Hud hud;
    private Grid grid;

    private Box2DDebugRenderer debugRenderer;
    private World world;

    private Array<Pared> contorno;

    private Array<Bloque> bloques;
    private Array<Rueda> ruedas;
    private Array<Rampa> rampas;
    private Array<Polea> poleas;

    private boolean pause;
    private int cuerpoActual = -1;
    private int obstaculoActual = -1;

    private float worldTime = 0f;

    private FisicaUI fisicaUI;

    public FisicaScreen()
    {
    }

    @Override
    public void show()
    {
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);

        Gdx.input.setInputProcessor(this);

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        viewport = new FitViewport(Gdx.graphics.getWidth()/PPM, Gdx.graphics.getHeight()/PPM);

        hud = new Hud(batch, width/2, height/2);
        grid = new Grid(0.5f*PPM, 0.5f*PPM, width, height);

        //mundo de Box2D
        world = new World(Constants.GRAVEDAD_2D, false);
        debugRenderer = new Box2DDebugRenderer();

        pause = true; //se empieza pausado

        contorno = new Array<Pared>(4);
        crearContorno(contorno, width, height); //dimensiones en pixeles

        bloques = new Array<Bloque>();
        ruedas = new Array<Rueda>();
        rampas = new Array<Rampa>();
        poleas = new Array<Polea>();

        //cargar el archivo de escenario
        FileLoader.loadSceneFile(world);

        ruedas.addAll(FileLoader.getRuedas());
        bloques.addAll(FileLoader.getBloques());
        rampas.addAll(FileLoader.getRampas());

        poleas.add(new Polea(world,
                new Vector2(1.5f, 3f),
                bloques.get(0),
                ruedas.get(0),
                0.4f));

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

        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        viewport.apply();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        for(Pared pared:contorno)
            pared.render(batch);

        for(Bloque bloque:bloques)
            bloque.render(batch);

        for(Rueda rueda:ruedas)
            rueda.render(batch);

        for(Rampa rampa: rampas)
            rampa.render(batch);

        for(Polea polea: poleas)
            polea.render(batch);
        batch.end();

        debugRenderer.render(world, viewport.getCamera().combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        renderer.begin();
        grid.render(renderer);
        renderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
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
            hud.update(1/60f);
            hud.setEntityState(ruedas.get(0).body.getPosition(),
                    ruedas.get(0).body.getLinearVelocity(),
                    1/60f);
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
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            force.x += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
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

/*        if(0 <= cuerpoActual && cuerpoActual < cuerpos.size)
        {
            cuerpos.get(cuerpoActual).body.applyForceToCenter(force, false);
        }*/
    }

    private void crearContorno(Array<Pared>contorno, float width, float height)
    {
        //piso
        contorno.add(new Pared(world,
                new Vector2(width/2/PPM, 0 - Constants.PARED_GROSOR/2),
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
                new Vector2(0 - Constants.PARED_GROSOR/2, height/2/PPM),
                Constants.PARED_GROSOR,
                height/PPM));
    }

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

            /*for(FisicaBody cuerpo:cuerpos)
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
            }*/
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
