package com.example.demo;

import MyGame.Sample.*;
import com.google.flatbuffers.FlatBufferBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * @author wuchonghua
 * @create 2022-06-06 13:30
 */
public class TestFlatBuffers {
    public static void main(String[] args) throws IOException {
        FlatBufferBuilder fbb = new FlatBufferBuilder(1);
        int name = fbb.createString("Frodo"); // 要放到startMonster之前
        int weaponsVector = Monster.createWeaponsVector(fbb, new int[]{
                Weapon.createWeapon(fbb, fbb.createString("w1"), (short) 21),
                Weapon.createWeapon(fbb, fbb.createString("w2"), (short) 22),
        }); // 要放到startMonster之前
        int inventoryVector = Monster.createInventoryVector(fbb, new byte[]{3, 4}); // 要放到startMonster之前
        Monster.startPathVector(fbb, 2);
        Vec3.createVec3(fbb, 103, 104, 105);
        Vec3.createVec3(fbb, 106, 107, 108);
        int path = fbb.endVector(); // 要放到startMonster之前

        Monster.startMonster(fbb);
        Monster.addName(fbb, name);
        Monster.addHp(fbb, (short)20);
        Monster.addColor(fbb, Color.Red);
        Monster.addWeapons(fbb, weaponsVector);
        Monster.addEquippedType(fbb, Equipment.Weapon);
        Monster.addPos(fbb, Vec3.createVec3(fbb, 100, 101, 102));
//        Monster.addMana(fbb, 151);
        Monster.addPath(fbb, path);
        Monster.addInventory(fbb, inventoryVector);
        int monsterEnd = Monster.endMonster(fbb);
        fbb.finish(monsterEnd);
        byte[] data = fbb.sizedByteArray();
        File file = new File("D:/1.bin");
        FileUtils.writeByteArrayToFile(file, data);

        File file1 = new File("D:/1.bin");
        RandomAccessFile f = new RandomAccessFile(file1, "r");
        byte[] bytes = new byte[(int)f.length()];
        f.readFully(bytes);
        f.close();
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        Monster monster = Monster.getRootAsMonster(bb);
        final Vec3.Vector vector = monster.pathVector();
        for (int i = 0; i < vector.length(); i++) {
            final Vec3 vec3 = vector.get(i);
            System.out.println(vec3.x() + " " + vec3.y() + " " + vec3.z());
        }

    }


}

