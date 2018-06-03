public class Label {
    static int count;

    public Label(){
        count = 0;
    }

    public void resetLabel(){
        count = 0;
    }

    public String newLabel(){
        return "R"+count++;
    }
}
