package kth.ag2411.mapalgebra;

import java.util.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Layer {
	public String name;
	public int nRows;
	public int nCols;;
	public double[] origin = new double[2];
	public double resolution;
	public double[] values; // data. Alternatively, public double[][] values;
	public double nullValue; // value designated as "No data"

	//Constructor 
	public Layer(String layerName, String fileName) {
		// You may want to do some work before reading a file.
		this.name = layerName;
		System.out.println("The name of layer:" + this.name);

		try { // Exception may be thrown while reading (and writing) a file.
			// Get access to the lines of Strings stored in the file
			// This object represents an input file, elevation.txt, located at ./data/.
			//File rFile = new File("./data/elevation.txt");
			File rFile = new File(fileName);
			// This object represents a stream of characters read from the file. 
			FileReader fReader = new FileReader(rFile);
			// This object represents lines of Strings created from the stream of characters.
			BufferedReader bReader = new BufferedReader(fReader); //这三句话是读文件的套装，file地址，freader file，制造一个缓冲区阅读文件

			// Read each line of Strings
			String text; // stores each line of Strings temporarily
			String words[];
			// Read first line, which starts with "ncols"
			text = bReader.readLine(); // first line is read
			System.out.println("this is the 1st line: " + text);
			if(text != null) {
				words = text.split(" ");
				String num_value = words[words.length - 1];
				this.nCols = Integer.parseInt(num_value);
			}

			//			this.nCols = 180;
			// Read second line, which starts with "nrows"
			text = bReader.readLine(); // second line is read
			System.out.println("this is the 2nd line: " + text);
			if(text != null) {
				words = text.split(" ");
				String num_value = words[words.length - 1];
				this.nRows = Integer.parseInt(num_value);
			}
			// Read third line, which starts with "xllcorner"
			text = bReader.readLine(); // second line is read
			System.out.println("this is the 3rd line: " + text);
			if(text != null) {
				words = text.split(" ");
				String num_value = words[words.length - 1];
				this.origin[0] = Integer.parseInt(num_value);
			}
			// Read forth line, which starts with "yllcorner"
			text = bReader.readLine(); // second line is read
			System.out.println("this is the 4th line: " + text);
			if(text != null) {
				words = text.split(" ");
				String num_value = words[words.length - 1];
				this.origin[1] = Integer.parseInt(num_value);
			}
			//this.origin[0] = xllcorner;
			//this.origin[1] = yllcorner;
			// Read fifth line, which starts with "cellsize"
			text = bReader.readLine(); // second line is read
			System.out.println("this is the 5th line: " + text);
			//this.resolution = 20;
			if(text != null) {
				words = text.split(" ");
				String num_value = words[words.length - 1];
				this.resolution = Integer.parseInt(num_value);
			}
			// Read sixth line, which starts with "NODATA_value"
			text = bReader.readLine(); // second line is read
			System.out.println("this is the 6th line: " + text);
			if(text != null) {
				words = text.split(" ");
				String num_value = words[words.length - 1];
				this.nullValue = Integer.parseInt(num_value);
			}
			// Read each of the remaining lines, which represents a row of raster
			// values
			int nPixels = this.nCols*this.nRows;
			values = new double [nPixels];
			// Read until the last line when the number of lines is not known
			text = bReader.readLine(); // first line is read
			int i = 0;
			double max = nullValue;
			double min = 100000000;
			while (text != null) { // Repeat the following until no more line to be read
				//System.out.println("this is the " + count + "the line: " + text);
				words = text.split(" ");
				for(int j = 0; j < nCols; j++) {
					double value = Double.parseDouble(words[j]);
					this.values[i*nCols+j] = value;
					if (value > max) {
						max = value;
					}
					if (value < min) {
						min = value;
					}

				}
				text = bReader.readLine(); // next line is read
				i = i + 1; // keep track of the number of lines so far
			}
			bReader.close();
			fReader.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Layer (String layerName, int nRows, int nCols, double[] origin, double resolution, double nullValue) {
		try{
			this.name = layerName;
			System.out.println("The name of layer:" + this.name);
			this.nRows = nRows;
			this.nCols = nCols;
			this.origin = origin;
			this.resolution = resolution;
			this.nullValue = nullValue;
			this.values = new double[nRows*nCols];
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Print (This is complete)
	public void print(){
		//Print this layer to console
		System.out.println("ncols "+nCols);
		System.out.println("nrows "+nRows);
		System.out.println("xllcorner "+origin[0]);
		System.out.println("yllcorner "+origin[1]);
		System.out.println("cellsize "+resolution);
		System.out.println("NODATA_value " + nullValue);
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				System.out.print(this.values[i*nCols+j]+" ");
			}
			System.out.println();
		}
	}

	// Save (This is not complete)
	@SuppressWarnings("resource")
	public void save(String outputFileName){
		// save this layer as an ASCII file that can be imported to ArcGIS
		try {// This object represents an output file, out.txt, located at ./data/.
			File file = new File(outputFileName); 
			if(!file.exists()) {
				file.createNewFile();// file.createNewFile();
			}
			// This object represents ASCII data (to be) stored in the file
			FileWriter fWriter = new FileWriter(file);

			// Write to the file
			fWriter.write("ncols              "+this.nCols+"\n"); // "\n" represents a new line
			fWriter.write("nrows              "+this.nRows+"\n"); // "\n" represents a new line
			fWriter.write("xllcorner          "+this.origin[0]+"\n"); // "\n" represents a new line
			fWriter.write("yllcorner          "+this.origin[1]+"\n"); // "\n" represents a new line
			fWriter.write("cellsize           "+this.resolution+"\n"); // "\n" represents a new line
			fWriter.write("NODATA_value       "+this.nullValue+"\n"); // "\n" represents a new line
			//writing the rest of the values
			for (int i = 0; i < nRows; i++) { 
				for (int j = 0; j < nCols; j++) {
					fWriter.write(this.values[i*nCols+j] + " ");
				}
				fWriter.write("\n");
			}
			fWriter.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double getMax() {
		double max = Double.MIN_VALUE;
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				if(values[i*nCols+j] > max) {
					max = values[i*nCols+j];
				}
			}
		}
		return max;
	}

	public double getMin() {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				if(values[i*nCols+j] < min) {
					min = values[i*nCols+j];
				}
			}
		}
		return min;
	}

	public BufferedImage toImage(){
		BufferedImage image = new BufferedImage(this.nCols, this.nRows, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = image.getRaster();
		double min = getMin();
		double max = getMax();
		for (int i = 0; i < this.nRows; i++) {
			for (int j = 0; j < this.nCols; j++) {
				double value = this.values[i*this.nRows+j];
				double value_strech = (255-(max-value)/(max-min))* 255 + 0;
				int[] color = new int[3];
				color[0] = (int)value_strech; // Red
				color[1] = (int)value_strech; // Green
				color[2] = (int)value_strech; // Blue

				raster.setPixel(j, i, color); // (19,0) is the pixel at the top-right corner.
			}
		}
		return image;
	}

	public BufferedImage toImage(double[] input){
		int length = input.length;
		// generating a legend of the colors that was the user input
		double[][] colors = new double[length][3];
		for (int i = 0; i < length; i++) {
			colors[i][0] = ThreadLocalRandom.current().nextInt(0, 255);
			colors[i][1] = ThreadLocalRandom.current().nextInt(0, 255);
			colors[i][2] = ThreadLocalRandom.current().nextInt(0, 255);
		}
		// assign color for each pixel
		BufferedImage image = new BufferedImage(this.nCols, this.nRows, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		for (int i = 0; i < this.nRows; i++) {
			for (int j = 0; j < this.nCols; j++) {
				double value = this.values[i*this.nRows+j];
				for (int k = 0; k < length; k++) {
					if (value == input[k]) {
						raster.setPixel(j, i, colors[k]);
					}			
				}
			}
		}
		return image;
	}

	public Layer localSum(Layer layer2, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows*nCols];
		if ((nRows == layer2.nRows) && (nCols == layer2.nCols)) {
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					outLayer.values[i*nCols+j] = this.values[i*nCols+j] + layer2.values[i*nCols+j];
				}
			}
		}
		else {
			System.out.println("The input layer does not match the exiting layer.");
		}

		return outLayer;
	}

	public Layer focalSum(int radius, boolean IsSquare, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows*nCols];
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				int[] neighborhood = getNeighborhood(i*nCols+j, radius, IsSquare);//the neighborhood’s shape is a square (if true) or a circle (if false)
				double sumNeigh = 0;
				for (int k = 0; k < neighborhood.length; k++) {
					int index = neighborhood[k];
					sumNeigh += this.values[index];
				}
				outLayer.values[i*nCols+j] = sumNeigh;
			}
		}	
		return outLayer;
	}

	public Layer focalProduct(int radius, boolean IsSquare, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows*nCols];
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				int[] neighborhood = getNeighborhood(i*nCols+j, radius, IsSquare);//the neighborhood’s shape is a square (if true) or a circle (if false)
				double sumNeigh = 0;
				for (int k = 0; k < neighborhood.length; k++) {
					int index = neighborhood[k];
					sumNeigh *= this.values[index];
				}
				outLayer.values[i*nCols+j] = sumNeigh;
			}
		}	
		return outLayer;
	}

	public Layer focalRanking(int radius, boolean IsSquare, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows*nCols];
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				int[] neighborhood = getNeighborhood(i*nCols+j, radius, IsSquare);//the neighborhood’s shape is a square (if true) or a circle (if false)
				HashMap<Integer, Double> neighMap = new HashMap<Integer, Double>();
				double rank = -1;
				for (int k = 0; k < neighborhood.length; k++) {
					neighMap.put(neighborhood[k], values[neighborhood[k]]);
				}
				List<Map.Entry<Integer, Double>> list = new ArrayList<>(neighMap.entrySet());
				Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>(){
					public int compare(Map.Entry<Integer, Double> o1,Map.Entry<Integer, Double> o2) {
						return o1.getKey().compareTo(o2.getKey());
					}
				});
//				Arrays.sort(neighValues, Collections.reverseOrder());
				for(int ii=0;ii<list.size();ii++) {
					if(list.get(ii).getKey()==i*nCols+j){
						rank = list.size()-ii;
					}
				}
				outLayer.values[i*nCols+j] = rank;
			}
		}	
		return outLayer;
	}

	public Layer focalVariety(int radius, boolean IsSquare, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows*nCols];
		//assign the values of outLayer
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				int[] neighborhood = getNeighborhood(i*nCols+j, radius, IsSquare);//the neighborhood’s shape is a square (if true) or a circle (if false)
				double[] values_neighbor = new double[neighborhood.length];
				for (int k = 0; k < neighborhood.length; k++) {
					int index = neighborhood[k];
					values_neighbor[k] = this.values[index];
				}
				outLayer.values[i*nCols+j] = gerNumUnique(values_neighbor);//the number of unique values in focal is variety
			}
		}	 
		return outLayer;
	}

	public Layer zonalSum(Layer zoneLayer, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows*nCols];
		if ((nRows != zoneLayer.nRows) || (nCols != zoneLayer.nCols)) {
			System.out.println("The input layer does not match the exiting layer.");
		}
		else {//create a dictionary for No. of zone and an array of all values
			HashMap<Double,ArrayList<Double>> hm = new HashMap<Double, ArrayList<Double>>();			
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					double key = zoneLayer.values[i*nCols+j];
					if(hm.containsKey(key)) { //key exists
						ArrayList<Double> zoneList = hm.get(key);
						zoneList.add(this.values[i*nCols+j]);//add new element
						hm.put(key, zoneList);	
					}
					else {//add key
						ArrayList<Double> zoneList = new ArrayList<Double>();
						hm.put(key, zoneList);
					}					
				}
			}
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					double key = zoneLayer.values[i*nCols+j];
					//https://blog.csdn.net/Android_Mrchen/article/details/107355803
					DoubleSummaryStatistics statistics = hm.get(key).stream().mapToDouble(Number::doubleValue).summaryStatistics();
					outLayer.values[i*nCols+j] = statistics.getSum();
				}
			}	
		}
		return outLayer;
	}

	public Layer zonalMean(Layer zoneLayer, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows*nCols];
		if ((nRows != zoneLayer.nRows) || (nCols != zoneLayer.nCols)) {
			System.out.println("The input layer does not match the exiting layer.");
		}
		else {//create a dictionary for No. of zone and an array of all values
			HashMap<Double,ArrayList<Double>> hm = new HashMap<Double, ArrayList<Double>>();			
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					double key = zoneLayer.values[i*nCols+j];
					if(hm.containsKey(key)) { //key exists
						ArrayList<Double> zoneList = hm.get(key);
						zoneList.add(this.values[i*nCols+j]);//add new element
						hm.put(key, zoneList);	
					}
					else {//add key
						ArrayList<Double> zoneList = new ArrayList<Double>();
						hm.put(key, zoneList);
					}					
				}
			}
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					double key = zoneLayer.values[i*nCols+j];
					DoubleSummaryStatistics statistics = hm.get(key).stream().mapToDouble(Number::doubleValue).summaryStatistics();
					outLayer.values[i*nCols+j] = statistics.getAverage();
					//System.out.println(statistics.getAverage());
				}
			}	
		}
		return outLayer;
	}

	public Layer zonalMaximum(Layer zoneLayer, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows*nCols];
		if ((nRows != zoneLayer.nRows) || (nCols != zoneLayer.nCols)) {
			System.out.println("The input layer does not match the exiting layer.");
		}
		else {//create a dictionary for No. of zone and Maximum
			HashMap<Double, Double> hm = new HashMap<Double, Double>();
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					double key = zoneLayer.values[i*nCols+j];
					if(hm.containsKey(key)) { //key exists
						if(this.values[i*nCols+j] > hm.get(key)) {
							hm.put(key, this.values[i*nCols+j]);//renew value
						}	
					}
					else {//add key
						hm.put(key, this.values[i*nCols+j]);
					}					
				}
			}
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					double key = zoneLayer.values[i*nCols+j];
					outLayer.values[i*nCols+j] = hm.get(key);				
				}
			}	
		}
		return outLayer;
	}

	public Layer zonalMinimum(Layer zoneLayer, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows*nCols];
		if ((nRows != zoneLayer.nRows) || (nCols != zoneLayer.nCols)) {
			System.out.println("The input layer does not match the exiting layer.");
		}
		else {
			//create a dictionary for No. of zone and Minimum
			HashMap<Double, Double> hm = new HashMap<Double, Double>();
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					double key = zoneLayer.values[i*nCols+j];
					if(hm.containsKey(key)) { //key exists
						if(this.values[i*nCols+j] < hm.get(key)) {
							hm.put(key, this.values[i*nCols+j]);//renew value
						}	
					}
					else {//add key
						hm.put(key, this.values[i*nCols+j]);
					}					
				}
			}
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					double key = zoneLayer.values[i*nCols+j];
					outLayer.values[i*nCols+j] = hm.get(key);				
				}
			}	
		}
		return outLayer;
	}

	public Layer zonalProduct(Layer zoneLayer, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows*nCols];
		if ((nRows != zoneLayer.nRows) || (nCols != zoneLayer.nCols)) {
			System.out.println("The input layer does not match the exiting layer.");
		}
		else {//create a dictionary for No. of zone and an array of all values
			HashMap<Double,ArrayList<Double>> hm = new HashMap<Double, ArrayList<Double>>();			
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					double key = zoneLayer.values[i*nCols+j];
					if(hm.containsKey(key)) { //key exists
						ArrayList<Double> zoneList = hm.get(key);
						zoneList.add(this.values[i*nCols+j]);//add new element
						hm.put(key, zoneList);	
					}
					else {//add key
						ArrayList<Double> zoneList = new ArrayList<Double>();
						hm.put(key, zoneList);
					}					
				}
			}
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					double key = zoneLayer.values[i*nCols+j];
					Double product = hm.get(key).stream().reduce((number, number2)-> number*number2).get();
					outLayer.values[i*nCols+j] = product.doubleValue();
				}
			}	
		}
		return outLayer;
	}

	public Layer zonalVariety(Layer zoneLayer, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows*nCols];
		if ((nRows != zoneLayer.nRows) || (nCols != zoneLayer.nCols)) {
			System.out.println("The input layer does not match the exiting layer.");
		}
		else {//create a dictionary for No. of zone and an array of all values
			HashMap<Double,ArrayList<Double>> hm = new HashMap<Double, ArrayList<Double>>();			
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					double key = zoneLayer.values[i*nCols+j];
					if(hm.containsKey(key)) { //key exists
						ArrayList<Double> zoneList = hm.get(key);
						zoneList.add(this.values[i*nCols+j]);//add new element
						hm.put(key, zoneList);	
					}
					else {//add key
						ArrayList<Double> zoneList = new ArrayList<Double>();
						hm.put(key, zoneList);
					}					
				}
			}
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					double key = zoneLayer.values[i*nCols+j];
					ArrayList<Double> zoneList = hm.get(key);
					double[] zonelist= new double[zoneList.size()];
					int counter = 0;
					for (Double zoneObj: zoneList) {
						zonelist[counter] = zoneObj;
						counter++;
					}
					outLayer.values[i*nCols+j] = gerNumUnique(zonelist);
				}
			}	
		}
		return outLayer;
	}

	//return the number of unique values in a double array
	private int gerNumUnique(double[] input) {
		//get the number of unique values in an array.
		Set<Double> set = new HashSet<Double>();
		for(int i = 0; i < input.length;i++) {
			set.add(input[i]);
		}
		return set.size();
	}

	//return an array of integer values representing the indices of the cells that belong to the neighborhood. 
	@SuppressWarnings("removal")
	private int[] getNeighborhood(int index, int radius, boolean IsSquare) {
		int row = index/nCols; // the row number of cell i
		int col = index%nCols; // the column number of cell i

		//assign the bourdary
		int row_begin = Math.max(0,row - radius);
		int row_end = Math.min(nRows-1, row + radius);
		int col_begin = Math.max(0, col - radius);
		int col_end = Math.min(nCols-1, col + radius);

		// number of rows and cols
		int num_rows = row_end - row_begin + 1;
		int num_cols = col_end - col_begin + 1;
		// get all indics
		int[] neighborhood;
		if (IsSquare) {
			neighborhood = new int[num_rows * num_cols];
			for (int i = 0; i < num_rows; i++) {
				for (int j = 0; j < num_cols; j++) {
					//ii=row_begin+i, jj=col_begin+j
					neighborhood[i*num_cols+j] = (row_begin + i)*nCols + (col_begin + j);
				}
			}
			return neighborhood;
		}
		else {//circle
			ArrayList<Integer> neighborList = new ArrayList<Integer>(); // With <Integer>, only 
			int neighbor; // Integer objects can be
			Integer neighborObj;  
			//neighborhood = new int[num_rows * num_cols];
			for (int i = 0; i < num_rows; i++) {
				for (int j = 0; j < num_cols; j++) {
					//ii=row_begin+i, jj=col_begin+j
					if((row-i-row_begin)*(row-i-row_begin)+(col-j-col_begin)*(col-j-col_begin) > radius*radius) {
						continue;
					}
					else {
						neighbor = (row_begin + i)*nCols + (col_begin + j);
						neighborObj = new Integer(neighbor);
						//System.out.println("circle values"+neighbor+" "+neighborObj);
						neighborList.add(neighborObj);
					}
				}
			}
			neighborhood = neighborList.stream().mapToInt(i->i).toArray();
			return neighborhood;
		}
	}






}
