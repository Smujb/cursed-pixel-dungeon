package com.shatteredpixel.yasd.general.levels.chapters.watertrial;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Bones;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.Nightmare;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.Patch;
import com.shatteredpixel.yasd.general.levels.chapters.caves.CavesLevel;
import com.shatteredpixel.yasd.general.levels.interactive.Entrance;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.tiles.DungeonTileSheet;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import org.jetbrains.annotations.NotNull;

import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.EMPTY;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.EMPTY_DECO;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.ENTRANCE;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WALL;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WALL_DECO;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WATER;

public class WaterTrialBossLevel extends Level {

    {
        color1 = 0x534f3e;
        color2 = 0xb9d661;

        viewDistance = Math.min(6, viewDistance);

        bossLevel = true;
    }

    @Override
    public int getScaleFactor() {
        return new WaterTrialLevel().getScaleFactor();
    }

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;

    private static final int ROOM_LEFT		= WIDTH / 2 - 3;
    private static final int ROOM_RIGHT		= WIDTH / 2 + 1;
    private static final int ROOM_TOP		= HEIGHT / 2 - 2;
    private static final int ROOM_BOTTOM	= HEIGHT / 2 + 2;

    private int arenaDoor;
    private boolean enteredArena = false;
    private boolean keyDropped = false;

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_SEWERS;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }

    @Override
    public String loadImg() {
        return Assets.Interfaces.LOADING_SEWERS;
    }

    private static final String DOOR	= "door";
    private static final String ENTERED	= "entered";
    private static final String DROPPED	= "droppped";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( DOOR, arenaDoor );
        bundle.put( ENTERED, enteredArena );
        bundle.put( DROPPED, keyDropped );
    }

    @Override
    public void restoreFromBundle(@NotNull Bundle bundle ) {
        super.restoreFromBundle( bundle );
        arenaDoor = bundle.getInt( DOOR );
        enteredArena = bundle.getBoolean( ENTERED );
        keyDropped = bundle.getBoolean( DROPPED );
    }

    @Override
    protected boolean build() {

        setSize(WIDTH, HEIGHT);

        Rect space = new Rect();

        space.set(
                Random.IntRange(2, 6),
                Random.IntRange(2, 6),
                Random.IntRange(width-6, width-2),
                Random.IntRange(height-6, height-2)
        );

        Painter.fillEllipse( this, space, EMPTY );

        setExit(-1);

        Painter.fill( this, ROOM_LEFT - 1, ROOM_TOP - 1,
                ROOM_RIGHT - ROOM_LEFT + 3, ROOM_BOTTOM - ROOM_TOP + 3, WALL );
        Painter.fill( this, ROOM_LEFT, ROOM_TOP + 1,
                ROOM_RIGHT - ROOM_LEFT + 1, ROOM_BOTTOM - ROOM_TOP, EMPTY );

        Painter.fill( this, ROOM_LEFT, ROOM_TOP,
                ROOM_RIGHT - ROOM_LEFT + 1, 1, EMPTY_DECO );

        arenaDoor = Random.Int( ROOM_LEFT, ROOM_RIGHT ) + (ROOM_BOTTOM + 1) * width();
        set(arenaDoor, Terrain.DOOR);

        int entrance = Random.Int( ROOM_LEFT + 1, ROOM_RIGHT - 1 ) +
                Random.Int( ROOM_TOP + 1, ROOM_BOTTOM - 1 ) * width();
        interactiveAreas.add(new Entrance().setPos(this, entrance));
        set(getEntrancePos(), ENTRANCE);

        boolean[] patch = Patch.generate( width, height, 0.30f, 6, true );
        for (int i=0; i < length(); i++) {
            if (getTerrain(i) == EMPTY && patch[i]) {
                set(i, WATER);
            }
        }

        for (int i=width() + 1; i < length() - width(); i++) {
            if (getTerrain(i) == EMPTY) {
                int n = 0;
                if (getTerrain(i+1) == WALL) {
                    n++;
                }
                if (getTerrain(i-1) == WALL) {
                    n++;
                }
                if (getTerrain(i+width()) == WALL) {
                    n++;
                }
                if (getTerrain(i-width()) == WALL) {
                    n++;
                }
                if (Random.Int( 8 ) <= n) {
                    set(i, EMPTY_DECO);
                }
            }
        }

        for (int i=0; i < length() - width(); i++) {
            if (getTerrain(i) == WALL
                    && DungeonTileSheet.floorTile(getTerrain(i + width()))
                    && Random.Int( 3 ) == 0) {
                set(i, WALL_DECO);
            }
        }

        return true;
    }

    @Override
    protected void createMobs() {
    }

    public Actor respawner() {
        return null;
    }

    @Override
    protected void createItems() {
        Item item = Bones.get();
        if (item != null) {
            int pos;
            do {
                pos = Random.IntRange( ROOM_LEFT, ROOM_RIGHT ) + Random.IntRange( ROOM_TOP + 1, ROOM_BOTTOM ) * width();
            } while (pos == getEntrancePos());
            drop( item, pos ).setHauntedIfCursed().type = Heap.Type.REMAINS;
        }
    }

    @Override
    public int randomRespawnCell(Char ch) {
        int cell;
        do {
            cell = Random.Int( length() );
        } while (!passable(cell)
                || !Char.canOccupy(ch, this, cell)
                || Actor.findChar(cell) != null
                || !outsideEntraceRoom(cell));
        return cell;
    }

    @Override
    public void occupyCell( Char ch ) {

        super.occupyCell( ch );

        if (!enteredArena && outsideEntraceRoom( ch.pos ) && ch == Dungeon.hero) {

            enteredArena = true;
            seal();

            for (Mob m : mobs){
                //bring the first ally with you
                if (m.alignment == Char.Alignment.ALLY && !m.properties().contains(Char.Property.IMMOVABLE)){
                    m.pos = Dungeon.hero.pos + (Random.Int(2) == 0 ? +1 : -1);
                    m.sprite.place(m.pos);
                    break;
                }
            }

            Nightmare boss = Mob.create( Nightmare.class );
            boss.state = boss.WANDERING;
            do {
                boss.pos = Random.Int( length() );
            } while (
                    !passable(boss.pos) ||
                            !outsideEntraceRoom( boss.pos ) ||
                            heroFOV[boss.pos]);
            GameScene.add( boss );

            set( arenaDoor, WALL );
            GameScene.updateMap( arenaDoor );
            Dungeon.observe();

            CellEmitter.get( arenaDoor ).start( Speck.factory( Speck.ROCK ), 0.07f, 10 );
            Camera.main.shake( 3, 0.7f );
            Sample.INSTANCE.play( Assets.Sounds.ROCKS );
        }
    }


    @Override
    //Must be triggered manually by boss death
    public void unseal() {
        super.unseal();
        CellEmitter.get( arenaDoor ).start( Speck.factory( Speck.ROCK ), 0.07f, 10 );

        set( arenaDoor, EMPTY_DECO );
        GameScene.updateMap( arenaDoor );
        Dungeon.observe();
    }

    private boolean outsideEntraceRoom(int cell ) {
        int cx = cell % width();
        int cy = cell / width();
        return cx < ROOM_LEFT-1 || cx > ROOM_RIGHT+1 || cy < ROOM_TOP-1 || cy > ROOM_BOTTOM+1;
    }

    @Override
    public String tileName( Terrain tile ) {
        switch (tile) {
            case GRASS:
                return Messages.get(CavesLevel.class, "grass_name");
            case HIGH_GRASS:
                return Messages.get(CavesLevel.class, "high_grass_name");
            case WATER:
                return Messages.get(CavesLevel.class, "water_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc( Terrain tile ) {
        switch (tile) {
            case ENTRANCE:
                return Messages.get(CavesLevel.class, "entrance_desc");
            case EXIT:
                return Messages.get(CavesLevel.class, "exit_desc");
            case HIGH_GRASS:
                return Messages.get(CavesLevel.class, "high_grass_desc");
            case WALL_DECO:
                return Messages.get(CavesLevel.class, "wall_deco_desc");
            case BOOKSHELF:
                return Messages.get(CavesLevel.class, "bookshelf_desc");
            default:
                return super.tileDesc( tile );
        }
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        CavesLevel.addCavesVisuals(this, visuals);
        return visuals;
    }
}
