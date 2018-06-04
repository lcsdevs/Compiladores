public class Memory {
    public static int count;
    public static int countTemp;

    public Memory(){
        count = 0;
        countTemp = 0;
    }

    public void resetTemp(){
        countTemp = 0;
    }

    public int toAllocateTemp(){
        int tmp = count;
        count+=16384;
        return tmp;
    }

    public int toAllocateLogic(){
        int tmp = count;
        count++;
        return tmp;
    }

    public int toAllocateCharacter(){
        int tmp = count;
        count++;
        return tmp;
    }

    public int toAllocateInteger(){
        int tmp = count;
        count+=2;
        return tmp;
    }

    public int toAllocateVectorInt(int value){
        int tmp = count;
        count+= 2 * value;
        return tmp;
    }

    public int toAllocateVectorChar(int value){
        int tmp = count;
        count+=value;
        return tmp;
    }

    public int toAllocateString(){
        int tmp = count;
        count+=256;
        return tmp;
    }

    public int toAllocateString(int tam){
        int tmp = count;
        count+=tam;
        return tmp;
    }

    public int newTemp(){
        return countTemp;
    }

    public int toAllocateTempLogic(){
        int tmp = countTemp;
        countTemp++;
        return tmp;
    }

    public int toAllocateTempCharacter(){
        int tmp = countTemp;
        countTemp++;
        return tmp;
    }

    public int toAllocateTempInteger(){
        int tmp = countTemp;
        countTemp+=2;
        return tmp;
    }

    public int toAllocateTempVector(){
        int tmp = countTemp;
        countTemp+=4000;
        return tmp;
    }

    public int toAllocateTempString(){
        int tmp = countTemp;
        countTemp+=256;
        return tmp;
    }
}
