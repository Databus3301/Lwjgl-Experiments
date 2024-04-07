package Tests;

import Render.Entity.Entity2D;
import Render.MeshData.Texturing.Font;
import Render.MeshData.Texturing.Texture;
import Render.MeshData.Texturing.TextureAtlas;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Model.ObjModel;
import Render.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class TestTextureAtlas extends Test {

    private TextureAtlas atlas;
    private Entity2D e, atlasE;
    private int index, fc, speed;

    public TestTextureAtlas() {
        super();
        speed = 170;
        //initFontTest();
        initTextureAtlasTest();
    }

    @Override
    public void OnUpdate(float dt){
        super.OnUpdate(dt);
        if(fc++ > speed) {
            fc = 0;
            index++;
        }
    }
    @Override
    public void OnRender(){
        super.OnRender();
        renderer.drawEntity2D(atlasE);
        renderer.drawEntity2D(e);

        //testFont();
        testTextureAtlas();
    }


    public void testTextureAtlas(){
        if(index >= atlas.getCols() * atlas.getRows())
            index = 0;
        e.getModel().replaceTextureCoords(atlas.getTexCoords(index));

        // Chat-GPT: track the position of the current tile in the atlas
        int columns = atlas.getCols();
        int row = index / columns;
        int col = index % columns;

        float x = atlasE.getPosition().x + col * atlas.getTileWidth() + atlasE.getScale().x/2f - 140 - atlas.getTileWidth()*2f - atlas.getTileWidth()/2f;
        float y = atlasE.getPosition().y - row * atlas.getTileHeight() - atlasE.getScale().y/2f + 70 - atlas.getTileHeight()/2f + row*atlas.getSpacing()*3;

        renderer.drawRect(new Vector4f(x, y, atlas.getTileWidth(), atlas.getTileHeight()));
    }
    public void initTextureAtlasTest(){
        //atlas = new TextureAtlas(new Texture("FlowerGame.png", 0), 10, 4, 10, 32, 32, 1);
        atlas = new TextureAtlas(new Texture("LinkAnim.png", 0), 10, 8, 10, 120, 130, 0); index=40;
        atlasE = new Entity2D(new Vector2f(-Window.dim.x/2f + 140, Window.dim.y/2f -140), ObjModel.SQUARE.clone(), atlas.getTexture(), Shader.TEXTURING);
        atlasE.scale(140, 140 / atlas.getAspect());


        e = new Entity2D(new Vector2f(), ObjModel.SQUARE.clone(), atlas.getTexture(), Shader.TEXTURING);
        e.getModel().replaceTextureCoords(atlas.getTexCoords(0));
        e.scale(96, 96/atlas.getTileAspect());
    }

    public void testFont(){
        String ascii = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        if(index >= ascii.length())
            index = 0;
        e.getModel().replaceTextureCoords(Font.RETRO.getTexCoords(ascii.charAt(index)));
    }
    public void initFontTest(){
        Texture atlasTexture = new Texture("fonts/oldschool_white.png", 0);
        atlasE = new Entity2D(new Vector2f(-Window.dim.x/2f + 140, Window.dim.y/2f -140), ObjModel.SQUARE.clone(), atlasTexture, Shader.TEXTURING);
        atlasE.scale(140, 140 / atlasTexture.getAspect());

        ObjModel model = ObjModel.SQUARE.clone();
        e = new Entity2D(new Vector2f(), model, Font.RETRO.getTexture(), Shader.TEXTURING);
        e.scale(70, 70/ Font.RETRO.getCharacterAspect());
    }
}
