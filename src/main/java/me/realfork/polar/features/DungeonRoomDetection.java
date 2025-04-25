package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DungeonRoomDetection {
    public static HashMap<Integer, String> roomCores = new HashMap<>();

    public DungeonRoomDetection() {
        roomCores.put(740310812, "Waterfall");
        roomCores.put(82232284, "Waterfall");
        roomCores.put(1379043687, "Waterfall");
        roomCores.put(-1971268623, "Waterfall");
        roomCores.put(497881745, "Raccoon");
        roomCores.put(365045229, "Spider");
        roomCores.put(-1361285742, "Spider");
        roomCores.put(1778566373, "Spider");
        roomCores.put(-1035453872, "Gold");
        roomCores.put(1451614295, "Gold");
        roomCores.put(-430117371, "Shadow Assassin");
        roomCores.put(1484567748, "Fairy");
        roomCores.put(-1169615458, "Mossy");
        roomCores.put(1432034198, "Mossy");
        roomCores.put(1896208123, "Mossy");
        roomCores.put(-667603340, "Banners");
        roomCores.put(1600132124, "Catwalk");
        roomCores.put(-1010346133, "Catwalk");
        roomCores.put(-1694830065, "Catwalk");
        roomCores.put(544418695, "Duncan");
        roomCores.put(1103121487, "Higher Blaze");
        roomCores.put(-1092103153, "Lower Blaze");
        roomCores.put(-1085327384, "Red Green");
        roomCores.put(274652966, "Entrance");
        roomCores.put(-1092072828, "Entrance");
        roomCores.put(1913969999, "Entrance");
        roomCores.put(-1358669872, "New Trap");
        roomCores.put(-23510667, "Wizard");
        roomCores.put(1958624830, "Wizard");
        roomCores.put(735485465, "Wizard");
        roomCores.put(-2130054003, "Blood");
        roomCores.put(-1113939414, "Dino Site");
        roomCores.put(-1425445617, "Dino Site");
        roomCores.put(-609789679, "Dino Site");
        roomCores.put(881396995, "Spikes");
        roomCores.put(1928619293, "Quiz");
        roomCores.put(-2131538192, "Hall");
        roomCores.put(86014075, "Bomb Defuse");
        roomCores.put(1313090868, "Redstone Warrior");
        roomCores.put(1819727964, "Redstone Warrior");
        roomCores.put(-1862968316, "Pit");
        roomCores.put(-258250108, "Pit");
        roomCores.put(265221970, "Pit");
        roomCores.put(430079089, "Pit");
        roomCores.put(-1267948931, "Flags");
        roomCores.put(284566079, "Flags");
        roomCores.put(1157102457, "Flags");
        roomCores.put(67929126, "Flags");
        roomCores.put(1958698161, "Tic Tac Toe");
        roomCores.put(-316384390, "Cages");
        roomCores.put(-1265317780, "Big Red Flag");
        roomCores.put(1989395652, "Balcony");
        roomCores.put(559495102, "Balcony");
        roomCores.put(1046920372, "Dueces");
        roomCores.put(-330702540, "Stairs");
        roomCores.put(-283979980, "Stairs");
        roomCores.put(-697693183, "Stairs");
        roomCores.put(606731747, "Stairs");
        roomCores.put(462413746, "Dome");
        roomCores.put(-1682285647, "Small Waterfall");
        roomCores.put(272954274, "Cage");
        roomCores.put(1350621298, "Steps");
        roomCores.put(-685489527, "Atlas");
        roomCores.put(745093020, "Atlas");
        roomCores.put(308515062, "Atlas");
        roomCores.put(-38388847, "Atlas");
        roomCores.put(84632407, "Dragon");
        roomCores.put(-2040489612, "Lava Ravine");
        roomCores.put(-294054018, "Lava Ravine");
        roomCores.put(-2053308786, "Lava Ravine");
        roomCores.put(-233562612, "Deathmite");
        roomCores.put(-1901273450, "Deathmite");
        roomCores.put(-1408070175, "Deathmite");
        roomCores.put(-999204041, "Blue Skulls");
        roomCores.put(1073658098, "Ice Path");
        roomCores.put(887024382, "Purple Flags");
        roomCores.put(701310376, "Purple Flags");
        roomCores.put(-872480083, "Long Hall");
        roomCores.put(1011477602, "Red Blue");
        roomCores.put(1607395895, "Red Blue");
        roomCores.put(-1794981292, "Red Blue");
        roomCores.put(1077887433, "Overgrown Chains");
        roomCores.put(1128554492, "Old Trap");
        roomCores.put(-1849552977, "Water");
        roomCores.put(526587049, "Rails");
        roomCores.put(-214948895, "Rails");
        roomCores.put(-1778261714, "Rails");
        roomCores.put(1937419120, "Rails");
        roomCores.put(2051424561, "Ice Fill");
        roomCores.put(884728242, "Ice Fill");
        roomCores.put(1073109158, "Mushroom");
        roomCores.put(-1643528240, "Rare Overgrown");
        roomCores.put(-1604951086, "Bridges");
        roomCores.put(-1989950542, "Bridges");
        roomCores.put(-755321869, "Creeper Beams");
        roomCores.put(-476788643, "Three Weirdos");
        roomCores.put(-1957538226, "Museum");
        roomCores.put(-2121384577, "Museum");
        roomCores.put(-1797804860, "Museum");
        roomCores.put(1514395908, "Museum");
        roomCores.put(52900193, "Quad Lava");
        roomCores.put(-1250912300, "Small Stairs");
        roomCores.put(-1496765468, "Jumping Skulls");
        roomCores.put(-1678546839, "King Midas");
        roomCores.put(2147431657, "Diagonal");
        roomCores.put(1144217017, "Diagonal");
        roomCores.put(-464769223, "Diagonal");
        roomCores.put(-1027066030, "Mural");
        roomCores.put(270637140, "Market");
        roomCores.put(-402574914, "Market");
        roomCores.put(-880417926, "Market");
        roomCores.put(-402028497, "Hallway");
        roomCores.put(351544339, "Hallway");
        roomCores.put(-1723662179, "Hallway");
        roomCores.put(-1644671806, "Hallway");
        roomCores.put(-2082251638, "Beams");
        roomCores.put(-1691958814, "Dip");
        roomCores.put(-204678789, "Basement");
        roomCores.put(-109725212, "Water Board");
        roomCores.put(-1639566599, "Quartz Knight");
        roomCores.put(153753389, "Quartz Knight");
        roomCores.put(-129760550, "Quartz Knight");
        roomCores.put(-742870398, "Quartz Knight");
        roomCores.put(1172966775, "Pressure Plates");
        roomCores.put(681797038, "Pressure Plates");
        roomCores.put(652570347, "Default");
        roomCores.put(1956609103, "Default");
        roomCores.put(1751890846, "Cell");
        roomCores.put(-1823353629, "Mirror");
        roomCores.put(1449723216, "Lots Of Floors");
        roomCores.put(487124604, "Teleport Maze");
        roomCores.put(-361911912, "Mines");
        roomCores.put(-39488099, "Mines");
        roomCores.put(1363618678, "Mines");
        roomCores.put(1227315161, "Mines");
        roomCores.put(-1390729196, "Sloth");
        roomCores.put(-499989468, "Withermancer");
        roomCores.put(-406356904, "Withermancer");
        roomCores.put(-1645219026, "Withermancer");
        roomCores.put(-2043617055, "Cathedral");
        roomCores.put(789869846, "Cathedral");
        roomCores.put(-1212586959, "Cathedral");
        roomCores.put(1956475445, "Cathedral");
        roomCores.put(841898152, "Gravel");
        roomCores.put(1127962661, "Gravel");
        roomCores.put(4304131, "Gravel");
        roomCores.put(-845911506, "Crypt");
        roomCores.put(331494915, "Crypt");
        roomCores.put(-1811244478, "Slabs");
        roomCores.put(1663174337, "Temple");
        roomCores.put(1308341800, "Double Diamond");
        roomCores.put(-1447684689, "Archway");
        roomCores.put(1440798119, "Archway");
        roomCores.put(-671152674, "Boulder");
        roomCores.put(307825200, "Boulder");
        roomCores.put(113272043, "Chains");
        roomCores.put(1667732153, "Skull");
        roomCores.put(2091387826, "Scaffolding");
        roomCores.put(259238824, "Cobble Wall Pillar");
        roomCores.put(-1359302282, "Buttons");
        roomCores.put(160943502, "Buttons");
        roomCores.put(823430452, "Buttons");
        roomCores.put(2123177620, "Buttons");
        roomCores.put(1858897577, "Overgrown");
        roomCores.put(-1746428299, "Knight");
        roomCores.put(749593273, "Grand Library");
        roomCores.put(1348435369, "Grand Library");
        roomCores.put(196766004, "Well");
        roomCores.put(718434953, "Well");
        roomCores.put(1955671195, "Well");
        roomCores.put(-673246822, "Andesite");
        roomCores.put(754378401, "Locked Away");
        roomCores.put(1986002687, "Sarcophagus");
        roomCores.put(-195263543, "Tomioka");
        roomCores.put(332584803, "Arrow Trap");
        roomCores.put(-325025964, "Melon");
        roomCores.put(1964904676, "Melon");
        roomCores.put(158528145, "Melon");
        roomCores.put(-1897192562, "End");
        roomCores.put(27598620, "Perch");
        roomCores.put(-671539463, "Multicolored");
        roomCores.put(474745227, "Painting");
        roomCores.put(1516049261, "Layers");
        roomCores.put(820948152, "Layers");
        roomCores.put(456930870, "Layers");
        roomCores.put(-1910037748, "Layers");
        roomCores.put(1522346451, "Supertall");
        roomCores.put(-1376632689, "Supertall");
        roomCores.put(291711773, "Supertall");
        roomCores.put(-46683467, "Supertall");
        roomCores.put(-1989372370, "Admin");
        roomCores.put(-224496952, "Silver Sword");
        roomCores.put(-1803705489, "Logs");
        roomCores.put(-1346033867, "Pedestal");
        roomCores.put(281551136, "Pedestal");
        roomCores.put(1172045122, "Prison Cell");
        roomCores.put(568565222, "Grass Ruin");
        roomCores.put(-786211724, "Grass Ruin");
        roomCores.put(-456244067, "Granite");
        roomCores.put(-338946136, "Mage");
        roomCores.put(1089356068, "Mage");
        roomCores.put(-1967474423, "Leaves");
        roomCores.put(348655632, "Redstone Key");
        roomCores.put(-615308028, "Drop");
        roomCores.put(1216268340, "Rare Pillars");
        roomCores.put(-1169880205, "Vinny 8 Ball");
        roomCores.put(-1666473430, "Golden Oasis");
        roomCores.put(153580070, "Black Flag");
        roomCores.put(-1497998508, "Sand Dragon");
        roomCores.put(1756685113, "Chambers");
        roomCores.put(368708000, "Chambers");
        roomCores.put(252800591, "Chambers");
        roomCores.put(164990589, "Doors");
        roomCores.put(1033794268, "Doors");
        roomCores.put(477318192, "Stone Window");
        roomCores.put(1965783806, "Tombstone");
        roomCores.put(-1519947323, "Lava Pit");
        roomCores.put(256380076, "Mini Rails");
        roomCores.put(988444684, "Carpets");
        roomCores.put(1890757664, "Carpets");
        roomCores.put(633179672, "Three Floors");
        roomCores.put(-701175845, "Rail Track");

    }

    public static HashMap<AxisAlignedBB, String> roomCoords = new HashMap<>();

    public static String lastRoom = "";
    public static Vec3 lastRoomMiddle;

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent e) {
        if (Main.notNull && Utils.inDungeons()) {
            if (roomCoords.size() < 36) {
                findDungeonEdges();
            } else {
                String currentRoom = findCurrentRoom(Main.mcPlayer.getPositionVector());
                Vec3 currentRoomMiddle = null;

                for (AxisAlignedBB aabb : roomCoords.keySet()) {
                    if (aabb.isVecInside(Main.mcPlayer.getPositionVector())) {
                        currentRoomMiddle = new Vec3(aabb.minX + 16, 0, aabb.minZ + 16);
                    }
                }

                if (!lastRoom.equals("") && !currentRoom.equals("") && lastRoomMiddle != null && currentRoomMiddle != null && !lastRoom.equals(currentRoom)) {

                    String direction = "";

                    Vec3 roomDifferences = currentRoomMiddle.subtract(lastRoomMiddle);

                    if (roomDifferences.xCoord > 0) {
                        direction = "+x";
                    } else if (roomDifferences.xCoord < 0) {
                        direction = "-x";
                    } else if (roomDifferences.zCoord > 0) {
                        direction = "+z";
                    } else if (roomDifferences.zCoord < 0) {
                        direction = "-z";
                    }

                    Main.sendMarkedChatMessage("Player entered new room! | " + currentRoom + " Dir: " + direction);
                    me.jacktym.aiomacro.rendering.LineRendering.origin = Main.mcPlayer.getPositionVector();
                }
                lastRoom = currentRoom;
                lastRoomMiddle = currentRoomMiddle;
            }
        } else {
            roomCoords.clear();
        }
    }

    public void findDungeonEdges() {
        for (int x = 1; x <= 6; x++) {
            for (int z = 1; z <= 6; z++) {
                int xPos = -217 + x * 32;
                int zPos = -217 + z * 32;

                if (Main.mcWorld.getChunkProvider().chunkExists(xPos, zPos)) {
                    String room = getRoom(xPos, zPos);
                    AxisAlignedBB aabb = new AxisAlignedBB(xPos - 16, 0, zPos - 16, xPos + 16, 100, zPos + 16);
                    if (aabb.isVecInside(Main.mcPlayer.getPositionVector())) {
                        System.out.println(room + " | " + getRoomCore(xPos, zPos) + " | " + aabb + " | " + x + "x" + z);
                    }
                    if (!room.equals("") && !roomCoords.toString().contains(aabb.toString())) {
                        roomCoords.put(aabb, room);
                    } else if (!roomCoords.toString().contains(aabb.toString())) {
                        System.out.println(x + "x" + z);
                    }
                } else {
                    roomCoords.clear();
                }
            }
        }
    }

    public String getRoom(int x, int z) {
        return roomCores.getOrDefault(getRoomCore(x, z), "");
    }

    public int getRoomCore(int x, int z) {
        List<String> blocks = new ArrayList<>();

        for (int y = 140; y >= 12; y--) {
            int id = Block.getIdFromBlock(Main.mcWorld.getBlockState(new BlockPos(x, y, z)).getBlock());
            if (id != 5 && id != 54) {
                blocks.add(String.valueOf(id));
            }
        }
        return String.join("", blocks).hashCode();
    }

    public static String findCurrentRoom(Vec3 playerPosition) {
        for (AxisAlignedBB aabb : roomCoords.keySet()) {
            if (aabb.isVecInside(playerPosition)) {
                return roomCoords.get(aabb);
            }
        }
        return "Error! Couldn't Find Room!";
    }
}