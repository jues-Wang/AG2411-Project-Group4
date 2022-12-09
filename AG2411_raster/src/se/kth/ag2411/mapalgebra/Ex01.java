package se.kth.ag2411.mapalgebra;

public class Ex01 {
	public static void main(String[] args) {
		if(args.length == 3){
			//Instantiate a layer
			Layer layer = new Layer(args[0], args[1]);
			//Printing it on the console
			layer.print();
			//Saving it to the file output.txt
			layer.save(args[2]);
		}
		else {
			System.out.println("Too many or few arguments......");
		}
	}
}
