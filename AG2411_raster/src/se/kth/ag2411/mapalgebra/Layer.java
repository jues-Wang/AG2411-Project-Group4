package se.kth.ag2411.mapalgebra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;



public class Layer {
	// Attributes (This is complete)
	public String name; // name of this layer
	public int nRows; // number of rows
	public int nCols; // number of columns
	public double[] origin = new double[2]; // x,y-coordinates of lower-left corner
	public double resolution; // cell size
	public double[][] values; // data. Alternatively, public double[][] values;
	public double nullValue; // value designated as "No data"
	public double pathValue; // For shortest path visualization
	public int[][] prev; // Where the previous node is stored for shortest path algorithms

	//Constructor (This is not complete)
	public Layer(String layerName, String fileName) {
		String text;
		String[] temp_val;
		
		this.name = layerName;

		try { // Exception may be thrown while reading (and writing) a file.
			File rFile = new File(fileName);
			FileReader fReader = new FileReader(rFile);
			BufferedReader bReader = new BufferedReader(fReader);
			// Get access to the lines of Strings stored in the file

			// Read first line, which starts with "ncols"
			text = bReader.readLine().substring(5).trim();
			nCols = Integer.parseInt(text);


			// Read second line, which starts with "nrows"
			text = bReader.readLine().substring(5).trim();
			nRows = Integer.parseInt(text);

			// Read third line, which starts with "xllcorner"
			text = bReader.readLine().substring(9).trim();
			origin[0] = Double.parseDouble(text);

			// Read forth line, which starts with "yllcorner"
			text = bReader.readLine().substring(9).trim();
			origin[1] = Double.parseDouble(text);

			// Read fifth line, which starts with "cellsize"
			text = bReader.readLine().substring(8).trim();
			resolution = Double.parseDouble(text);

			// Read sixth line, which starts with "NODATA_value"
			text = bReader.readLine().substring(12).trim();
			nullValue = Double.parseDouble(text);

			// Read each of the remaining lines, which represents a row of raster values
			values = new double[nRows][nCols];
			text = bReader.readLine();
			int count = 0;
			while (text != null) {
				temp_val = text.split(" ");

				for (int i = 0; i < nCols; i++) {
					try {
						values[count][i] = Double.parseDouble(temp_val[i]);
					} catch (NumberFormatException ex) {
						ex.printStackTrace();
					}
				}
				count++;
				text = bReader.readLine();
			}
			bReader.close();
			
			prev = new int[nRows][nCols];
			pathValue = nullValue;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Layer(String outLayerName, int nRows, int nCols, double[] origin, double resolution, double nullValue) {
			// construct a new layer by assigning a value to each of its attributes
		this.name = outLayerName; // on the left hand side are the attributes of
		this.nRows = nRows; // the new layer;
		this.nCols = nCols; // on the right hand side are the parameters.
		this.origin = origin;
		this.resolution = resolution;
		this.nullValue = nullValue;
		values = new double[nRows][nCols];
		prev = new int[nRows][nCols];
		pathValue = nullValue;
			// to be continued...
	}
	
	// Print (This is complete)
	public void print(){

		//Print this layer to console
		System.out.println("ncols         "+nCols);
		System.out.println("nrows         "+nRows);
		System.out.println("xllcorner     "+origin[0]);
		System.out.println("yllcorner     "+origin[1]);
		System.out.println("cellsize      "+resolution);
		System.out.println("NODATA_value  "+nullValue);

		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				System.out.print(values[i][j] + " ");
			}
			System.out.print("\n");
		}
	}

	// Save (This is not complete)
	public void save(String outputFileName) {
		// save this layer as an ASCII file that can be imported to ArcGIS
		File file = new File(outputFileName);
		try {
			FileWriter fWriter = new FileWriter(file);
			fWriter.write("ncols         "+nCols+"\n");
			fWriter.write("nrows         "+nRows+"\n");
			fWriter.write("xllcorner     "+origin[0]+"\n");
			fWriter.write("yllcorner     "+origin[1]+"\n");
			fWriter.write("cellsize      "+resolution + "\n");
			fWriter.write("NODATA_value  " + nullValue+"\n");

			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					fWriter.write(values[i][j] + " ");
				}
				fWriter.write("\n");
			}
			fWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Image methods
	public BufferedImage toImage() {
		// create a BufferedImage of the layer in grayscale
		BufferedImage image = new BufferedImage(nCols, nRows, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		double maxNum = this.getMax();
		double minNum = this.getMin();
		double range = maxNum - minNum;
		
		int[] noValueColor = new int[3];
		noValueColor[0] = TestGUI.highlightColor.getRed();
		noValueColor[1] = TestGUI.highlightColor.getGreen();
		noValueColor[2] = TestGUI.highlightColor.getBlue();
		
		int[] pathColor = new int[3];
		pathColor[0] = TestGUI.crazyColor.getRed();
		pathColor[1] = TestGUI.crazyColor.getGreen();
		pathColor[2] = TestGUI.crazyColor.getBlue();
		
		
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				if (values[i][j] == nullValue) {
					raster.setPixel(j, i, noValueColor);
				} else if (values[i][j] == pathValue) {
					raster.setPixel(j, i, pathColor);
				} else {
					int[] color = new int[3];
					int temp_color = (int)Math.round((maxNum - values[i][j]) * 255 / range);
					color[0] = temp_color;
					color[1] = temp_color;
					color[2] = temp_color;
					raster.setPixel(j, i, color);
				}
			}
		}
		return image;
	}
	
	public BufferedImage toImage(double[] v) {
		// visualize a BufferedImage of the layer in color
		BufferedImage image = new BufferedImage(nCols, nRows, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		int length = v.length;
		int[][] colors = new int[length][3];
		
		int[] noValueColor = new int[3];
		noValueColor[0] = 255;
		noValueColor[1] = 208;
		noValueColor[2] = 47;
		
		// Create color palette
		for (int k = 0; k < length; k++) {
			int[] randColor = new int[3];
			randColor[0] = (int)Math.round(Math.random()*255);
			randColor[1] = (int)Math.round(Math.random()*255);
			randColor[2] = (int)Math.round(Math.random()*255);
			colors[k] = randColor;
		}
		
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				int[] color = new int[3];
				
				// Check if values of interest and apply the correct color
				for (int k = 0; k < length; k++) {
					if (values[i][j] == nullValue) {
						raster.setPixel(j, i, noValueColor);
					}
					
					if (v[k] == values[i][j]) {
						color[0] = colors[k][0];
						color[1] = colors[k][1];
						color[2] = colors[k][2];
						break;
					}
				}
				raster.setPixel(j, i, color);
			}
		}
		return image;
	}
	
	public BufferedImage toImage(Color color) { 
		int[] maxColor = new int[3];
		maxColor[0] = color.getRed();
		maxColor[1] = color.getGreen();
		maxColor[2] = color.getBlue();
		
		int[] minColor = new int[3];
		minColor[0] = 255;
		minColor[1] = 255;
		minColor[2] = 255;
		
		int[] noValueColor = new int[3];
		if(color.equals(new Color(255, 255, 0))) {
			noValueColor[0] = 0;
			noValueColor[1] = 0;
			noValueColor[1] = 0;
		}
		else {
			noValueColor[0] = TestGUI.highlightColor.getRed();
			noValueColor[1] = TestGUI.highlightColor.getGreen();
			noValueColor[2] = TestGUI.highlightColor.getBlue();
		}
		
		int[] pathColor = new int[3];
		pathColor[0] = 255 - maxColor[0];
		pathColor[1] = 255 - maxColor[1];
		pathColor[2] = 255 - maxColor[2];
		
		double redRange = Math.max(Math.abs(maxColor[0] - minColor[0]), 1);
		double greenRange = Math.max(Math.abs(maxColor[1] - minColor[1]), 1);
		double blueRange = Math.max(Math.abs(maxColor[2] - minColor[2]), 1);
		
		BufferedImage image = new BufferedImage(nCols, nRows, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		double maxNum = getMax();
		double minNum = getMin();
		double range = maxNum - minNum;
		
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) { // oldMin + (oldMax - oldMin) * (value - newMin) / (newMax - newMin)
				if (values[i][j] == nullValue) {
					raster.setPixel(j, i, noValueColor);
				} else if (values[i][j] == pathValue) {
					raster.setPixel(j, i, pathColor);
				} else {
					int[] pixelColor = new int[3]; // (maxNum - values[i][j]) * 255 / range
					pixelColor[0] = (int)Math.round(255 - (values[i][j] - minNum) * redRange / range) ;
					pixelColor[1] = (int)Math.round(255 - (values[i][j] - minNum) * greenRange / range);
					pixelColor[2] = (int)Math.round(255 - (values[i][j] - minNum) * blueRange / range);
					raster.setPixel(j, i, pixelColor);
				}
				
				
			}
		}
		
		return image;
	}
	
	public BufferedImage toImageLearning(double maxNum, double minNum, double visitValue, double highlightValue) {
		// create a BufferedImage of the layer in grayscale
		BufferedImage image = new BufferedImage(nCols, nRows, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		double range = maxNum - minNum;
		
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				int[] color = new int[3];
				if (values[i][j] == visitValue) {
					color[0] = 255;
					color[1] = 0;
					color[2] = 0;
					raster.setPixel(j, i, color);
				} else if (values[i][j] == highlightValue) {
					color[0] = 0;
					color[1] = 0;
					color[2] = 255;
					raster.setPixel(j, i, color);
				} else {
					int temp_color = (int)Math.round((maxNum - values[i][j]) * 255 / range);
					color[0] = temp_color;
					color[1] = temp_color;
					color[2] = temp_color;
					raster.setPixel(j, i, color);
				}
			}
		}
		return image;
	}
	
////// Local methods ///////////////////////////////////////////////////////////////////////////////
	
	// LocolSum: 
	// Create a new Layer in which the value of each pixel is 
	// the sum of counterparts in this Layer and in an input Layers.
	//        
	// Input:	inLayer: 		an input Layer ready for addition           
	// 			outLayerName: 	the name of output Layer
	//         
	// Output:	outLayer: 		an new Layer, value of each pixel: outLayer = (this)Layer + inLayer
	//
	public Layer localSum(Layer inLayer, String outLayerName){
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		for(int i = 0; i < nRows; i++){
			for (int j = 0; j < nCols; j++) {
				outLayer.values[i][j] = values[i][j] + inLayer.values[i][j];
			}
		}
		return outLayer;
	}
	
	public void localSumTest(Layer inLayer, String outLayerName, JLayeredPane layeredPane){
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		for(int i = 0; i < nRows; i++){
			for (int j = 0; j < nCols; j++) {
				outLayer.values[i][j] = values[i][j] + inLayer.values[i][j];
			}
		}
		TestGUI.getMapStartX();
		TestGUI.getMapStartY();
		
		if (TestGUI.mPanel != null) {
			TestGUI.layeredPane.remove(TestGUI.mPanel);
			TestGUI.layeredPane.revalidate();
			TestGUI.layeredPane.repaint();
		}
		BufferedImage image = outLayer.toImage();
		MapPanel map = new MapPanel(image, TestGUI.scale);
		TestGUI.layeredPane.add(map);
		map.setBounds(TestGUI.mapStartX, TestGUI.mapStartY, 2000, 2000);	
		map.setExtendedState(JFrame.MAXIMIZED_BOTH);

		TestGUI.layeredPane.revalidate();
		TestGUI.layeredPane.repaint();
		
	}
	
	// LocolDifference: 
	// Create a new Layer in which the value of each pixel is the result of 
	// counterpart in this Layer subtracts from in an input Layers.
	//          
	// Input:	inLayer: 		an input Layer ready for subtract           
	// 			outLayerName: 	the name of output Layer
	//         
	// Output:	outLayer: 		an new Layer, value of each pixel: outLayer = (this)Layer - inLayer
	//
	public Layer localDifference(Layer inLayer, String outLayerName){
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		for(int i = 0; i < nRows; i++){
			for (int j = 0; j < nCols; j++) {
				outLayer.values[i][j] = values[i][j] - inLayer.values[i][j];
			}
		}
		return outLayer;
	}
	
////// Focal methods ///////////////////////////////////////////////////////////////////////////////
	
	// FocalSum:
	// Create a new Layer in which the value of each pixel is the sum of 
	// its neighborhood in this Layer. The radius and shape of neighborhood
	// should be given as parameters.
	//          
	// Input:	radius: 		the radius of neighborhood           
	// 			IsSquare: 		the shape of neighborhood, true--square, false--circle
	// 			outLayerName: 	the name of output Layer
	//         
	// Output:	outLayer: 		an new Layer, value of each pixel: 
	// 							outLayer = neighborhood[0]+neighborhood[1]+...+neighborhood[n]
	//
	public Layer focalSum(int radius, boolean IsSquare, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows][nCols];
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				//the neighborhood’s shape is a square (if true) or a circle (if false)
				int[][] neighborhood = getNeighborhood(i*nCols+j, radius, IsSquare);
				double sumNeigh = 0;
				for (int k = 0; k < neighborhood.length; k++) {
					int l = neighborhood[k][0];
					int m = neighborhood[k][1];
					sumNeigh += this.values[l][m];
				}
				outLayer.values[i][j] = sumNeigh;
			}
		}	
		return outLayer;
	}
	
	// FocalProduct:
	// Create a new Layer in which the value of each pixel is the product of 
	// its neighborhood in this Layer. The radius and shape of neighborhood
	// should be given as parameters.
	//          
	// Input:	radius: 		the radius of neighborhood           
	// 			IsSquare: 		the shape of neighborhood, true--square, false--circle
	// 			outLayerName: 	the name of output Layer
	//         
	// Output:	outLayer: 		an new Layer, value of each pixel:
	// 							outLayer = neighborhood[0]*neighborhood[1]*...*neighborhood[n]
	//
	public Layer focalProduct(int radius, boolean IsSquare, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows][nCols];
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				int[][] neighborhood = getNeighborhood(i*nCols+j, radius, IsSquare);//the neighborhood’s shape is a square (if true) or a circle (if false)
				double sumNeigh = 0;
				for (int k = 0; k < neighborhood.length; k++) {
					int l = neighborhood[k][0];
					int m = neighborhood[k][1];
					sumNeigh *= this.values[l][m];
				}
				outLayer.values[i][j] = sumNeigh;
			}
		}	
		return outLayer;
	}
	
	// FocalRanking:
	// Create a new Layer in which the value of each pixel is its rank in
	// its neighborhood in this Layer. The radius and shape of neighborhood
	// should be given as parameters.
	//          
	// Input:	radius: 		the radius of neighborhood           
	// 			IsSquare: 		the shape of neighborhood, true--square, false--circle
	// 			outLayerName: 	the name of output Layer
	//         
	// Output:	outLayer: 		an new Layer, value of each pixel:
	// 							outLayer = its rank in neighborhood(Descending order)
	//
	public Layer focalRanking(int radius, boolean IsSquare, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows][nCols];
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				int[][] neighborhood = getNeighborhood(i*nCols+j, radius, IsSquare);//the neighborhood’s shape is a square (if true) or a circle (if false)
				HashMap<Integer, Double> neighMap = new HashMap<Integer, Double>();
				double rank = -1;
				for (int k = 0; k < neighborhood.length; k++) {
					int l = neighborhood[k][0];
					int m = neighborhood[k][1];
					neighMap.put(l*nCols+j, values[l][m]);
				}
				List<Map.Entry<Integer, Double>> list = new ArrayList<>(neighMap.entrySet());
				//sort in ascending order
				Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>(){
					public int compare(Map.Entry<Integer, Double> o1,Map.Entry<Integer, Double> o2) {
						return o1.getKey().compareTo(o2.getKey());
					}
				});
//				Arrays.sort(neighValues, Collections.reverseOrder());
				for(int ii=0;ii<list.size();ii++) {
					if(list.get(ii).getKey()==i*nCols+j){
						rank = list.size()-ii;//descending order
					}
				}
				outLayer.values[i][j] = rank;
			}
		}	
		return outLayer;
	}
	
	// FocalVariety:
	// Create a new Layer in which the value of each pixel is the number of 
	// unique values in its neighborhood. The radius and shape of neighborhood
	// should be given as parameters.
	//          
	// Input:	radius: 		the radius of neighborhood           
	// 			IsSquare: 		the shape of neighborhood, true--square, false--circle
	// 			outLayerName: 	the name of output Layer
	//         
	// Output:	outLayer: 		an new Layer, value of each pixel:
	// 							outLayer = the number of unique values in neighborhood
	//					   e.g. the number of unique values in [1, 0, 2 , 1, 0, 0, 2] = 3
	//
	public Layer focalVariety(int r, boolean isSquare, String outLayerName) {
		 Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
	        for(int i = 0; i < nRows; i++) {
	            for (int j = 0; j < nCols; j++) {
	                int input = i * nCols + j;
	                int[][] neighborhood = outLayer.getNeighborhood(input, r, isSquare);
	                
	                // Create list of unique values for specified neighborhood
	                ArrayList<Double> list = new ArrayList<Double>();
	                for (int[] neighbor: neighborhood) {
	                    int row = neighbor[0];
	                    int col = neighbor[1];
	                    
	                    Double valueObj = values[row][col];
	                    
	                    if (!list.contains(valueObj)) {
	                        list.add(valueObj);
	                    }
	                }
	                outLayer.values[i][j] = list.size();
	            }
	        }
	        return outLayer;
	}
	
	
	
////// Zonal methods ///////////////////////////////////////////////////////////////////////////////
	
	// ZonalSum
	// Create a new Layer in which the value of each pixel is the sum of 
	// its zone in this Layer. 
	//          
	// Input:	zoneLayer: 		a Layer that divide this layer into several zones by unique numbers.
	// 			outLayerName: 	the name of output Layer
	//         
	// Output:	outLayer: 		an new Layer, value of each pixel:
	// 							outLayer = the sum of zone
    //
	public Layer zonalSum(Layer zoneLayer, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows][nCols];
		if ((nRows != zoneLayer.nRows) || (nCols != zoneLayer.nCols)) {
			System.out.println("The input layer does not match the exiting layer.");
		}
		else {//create a dictionary for No. of zone and an array of all values
			HashMap<Double,ArrayList<Double>> hm = new HashMap<Double, ArrayList<Double>>();			
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					double key = zoneLayer.values[i][j];
					if(hm.containsKey(key)) { //key exists
						ArrayList<Double> zoneList = hm.get(key);
						zoneList.add(this.values[i][j]);//add new element
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
					double key = zoneLayer.values[i][j];
					//https://blog.csdn.net/Android_Mrchen/article/details/107355803
					DoubleSummaryStatistics statistics = hm.get(key).stream().mapToDouble(Number::doubleValue).summaryStatistics();
					outLayer.values[i][j] = statistics.getSum();
				}
			}	
		}
		return outLayer;
	}
	
	// ZonalMean
	// Create a new Layer in which the value of each pixel is the mean of 
	// its zone in this Layer. 
	//          
	// Input:	zoneLayer: 		a Layer that divide this layer into several zones by unique numbers.
	// 			outLayerName: 	the name of output Layer
	//         
	// Output:	outLayer: 		an new Layer, value of each pixel:
	// 							outLayer = the mean of zone
    //
	public Layer zonalMean(Layer zoneLayer, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows][nCols];
		if ((nRows != zoneLayer.nRows) || (nCols != zoneLayer.nCols)) {
			System.out.println("The input layer does not match the exiting layer.");
		}
		else {//create a dictionary for No. of zone and an array of all values
			HashMap<Double,ArrayList<Double>> hm = new HashMap<Double, ArrayList<Double>>();			
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					double key = zoneLayer.values[i][j];
					if(hm.containsKey(key)) { //key exists
						ArrayList<Double> zoneList = hm.get(key);
						zoneList.add(this.values[i][j]);//add new element
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
					double key = zoneLayer.values[i][j];
					DoubleSummaryStatistics statistics = hm.get(key).stream().mapToDouble(Number::doubleValue).summaryStatistics();
					outLayer.values[i][j] = statistics.getAverage();
					//System.out.println(statistics.getAverage());
				}
			}	
		}
		return outLayer;
	}
	
	// ZonalMaximum
	// Create a new Layer in which the value of each pixel is the maximum of 
	// its zone in this Layer. 
	//          
	// Input:	zoneLayer: 		a Layer that divide this layer into several zones by unique numbers.
	// 			outLayerName: 	the name of output Layer
	//         
	// Output:	outLayer: 		an new Layer, value of each pixel:
	// 							outLayer = the maximum of zone
    //
	public Layer zonalMaximum(Layer zoneLayer, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows][nCols];
		if ((nRows != zoneLayer.nRows) || (nCols != zoneLayer.nCols)) {
			System.out.println("The input layer does not match the exiting layer.");
		}
		else {//create a dictionary for No. of zone and Maximum
			HashMap<Double, Double> hm = new HashMap<Double, Double>();
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					double key = zoneLayer.values[i][j];
					if(hm.containsKey(key)) { //key exists
						if(this.values[i][j] > hm.get(key)) {
							hm.put(key, this.values[i][j]);//renew value
						}	
					}
					else {//add key
						hm.put(key, this.values[i][j]);
					}					
				}
			}
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					double key = zoneLayer.values[i][j];
					outLayer.values[i][j] = hm.get(key);				
				}
			}	
		}
		return outLayer;
	}
	
	// ZonalMinimum
	// Create a new Layer in which the value of each pixel is the minimum of 
	// its zone in this Layer. 
	//          
	// Input:	zoneLayer: 		a Layer that divide this layer into several zones by unique numbers.
	// 			outLayerName: 	the name of output Layer
	//         
	// Output:	outLayer: 		an new Layer, value of each pixel:
	// 							outLayer = the minimum of zone
    //
	public Layer zonalMinimum(Layer zoneLayer, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		
		// Create hashmap with associated lowest values
		HashMap<Double, Double> hm = new HashMap<Double, Double>();
		for(int i = 0; i < nRows; i++){
			for (int j = 0; j < nCols; j++) {
				Double key = zoneLayer.values[i][j];
				Double value = values[i][j];
				if (!hm.containsKey(key) || hm.get(key) > value) {
					hm.put(key,  value);
				}
			}
		}
		// Assign lowest values to output layer
		for(int i = 0; i < nRows; i++){
			for (int j = 0; j < nCols; j++) {
				Double key = zoneLayer.values[i][j];
				outLayer.values[i][j] = hm.get(key);
			}
		}
		return outLayer;
	}
	
	// ZonalProduct
	// Create a new Layer in which the value of each pixel is the product of 
	// its zone in this Layer. 
	//          
	// Input:	zoneLayer: 		a Layer that divide this layer into several zones by unique numbers.
	// 			outLayerName: 	the name of output Layer
	//         
	// Output:	outLayer: 		an new Layer, value of each pixel:
	// 							outLayer = the product of zone
    //
	public Layer zonalProduct(Layer zoneLayer, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows][nCols];
		if ((nRows != zoneLayer.nRows) || (nCols != zoneLayer.nCols)) {
			System.out.println("The input layer does not match the exiting layer.");
		}
		else {//create a dictionary for No. of zone and an array of all values
			HashMap<Double,ArrayList<Double>> hm = new HashMap<Double, ArrayList<Double>>();			
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					double key = zoneLayer.values[i][j];
					if(hm.containsKey(key)) { //key exists
						ArrayList<Double> zoneList = hm.get(key);
						zoneList.add(this.values[i][j]);//add new element
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
					double key = zoneLayer.values[i][j];
					Double product = hm.get(key).stream().reduce((number, number2)-> number*number2).get();
					outLayer.values[i][j] = product.doubleValue();
				}
			}	
		}
		return outLayer;
	}
	
	// ZonalVariety
	// Create a new Layer in which the value of each pixel is the number of 
	// the unique values in its zone in this Layer. 
	//          
	// Input:	zoneLayer: 		a Layer that divide this layer into several zones by unique numbers.
	// 			outLayerName: 	the name of output Layer
	//         
	// Output:	outLayer: 		an new Layer, value of each pixel:
	// 							outLayer = the number of unique values in its zone
	//					   e.g. the number of unique values in [1, 0, 2 , 1, 0, 0, 2] = 3
	//
	public Layer zonalVariety(Layer zoneLayer, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		outLayer.values = new double[nRows][nCols];
		if ((nRows != zoneLayer.nRows) || (nCols != zoneLayer.nCols)) {
			System.out.println("The input layer does not match the exiting layer.");
		}
		else {//create a dictionary for No. of zone and an array of all values
			HashMap<Double,ArrayList<Double>> hm = new HashMap<Double, ArrayList<Double>>();			
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					double key = zoneLayer.values[i][j];
					if(hm.containsKey(key)) { //key exists
						ArrayList<Double> zoneList = hm.get(key);
						zoneList.add(this.values[i][j]);//add new element
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
					double key = zoneLayer.values[i][j];
					ArrayList<Double> zoneList = hm.get(key);
					double[] zonelist= new double[zoneList.size()];
					int counter = 0;
					for (Double zoneObj: zoneList) {
						zonelist[counter] = zoneObj;
						counter++;
					}
					outLayer.values[i][j] = getNumUnique(zonelist);
				}
			}	
		}
		return outLayer;
	}
	
	// Distance methods
	public double[] getDistance(int startIndex, int endIndex) {
		int startRow = startIndex / nCols;
		int startCol = startIndex % nCols;
		int endRow = endIndex / nCols;
		int endCol = endIndex % nCols;
		double[] outDistances = new double[2];
		
		double manhattan = Math.abs(startRow - endRow) + Math.abs(startCol - endCol);
		double euclidean = Math.sqrt( Math.pow(startRow - endRow, 2) + Math.pow(startCol - endCol, 2) );
		
		outDistances[0] = euclidean * resolution;
		outDistances[1] = manhattan * resolution;
		
		return outDistances;
	}
	
	// This is not the fastest implementation but it works
	public Layer dijkstra(String outLayerName, int startIndex) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		int startRow = startIndex / nCols;
		int startCol = startIndex % nCols;
		
		// Create list of temporary nodes and assign temporary values to  
		HashMap<String, Integer> tempNodes = new HashMap<String, Integer>();
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				int index = i* nCols + j;
				tempNodes.put(index + "", index);
				outLayer.values[i][j] = Double.POSITIVE_INFINITY;
			}
		}
		
		// Initialize
		outLayer.values[startRow][startCol] = 0;
		outLayer.prev[startRow][startCol] = -1;
		
		
		while (! tempNodes.isEmpty()) {
			Map.Entry<String,Integer> entry = tempNodes.entrySet().iterator().next();
			String visitIndexString = entry.getKey();
			int visitIndex = entry.getValue();
			
			double min = Double.POSITIVE_INFINITY;
			
			// Get minimum value of temporary nodes
			for (String tempNode: tempNodes.keySet()) {
				int tempRow = tempNodes.get(tempNode) / nCols;
				int tempCol = tempNodes.get(tempNode) % nCols;
				if (outLayer.values[tempRow][tempCol] < min) {
					visitIndexString = tempNode;
					visitIndex = tempNodes.get(tempNode);
					min = outLayer.values[tempRow][tempCol];
				}
			}
			
			int visitRow = visitIndex / nCols;
			int visitCol = visitIndex % nCols;
			int[][] neighborhood = getNeighborhood(visitIndex, 1, true);
			
			double newWeight;
			for (int[] neighbor: neighborhood) {
				int i = neighbor[0];
				int j = neighbor[1];
				
				double d = Math.sqrt( Math.pow(i - visitRow, 2) + Math.pow(j - visitCol, 2) );
				newWeight = outLayer.values[visitRow][visitCol] + d * values[i][j];
				
				if (newWeight < outLayer.values[i][j]) {
					outLayer.values[i][j] = newWeight;
					outLayer.prev[i][j] = visitIndex;
				}
			}
			tempNodes.remove(visitIndexString);
		}
		
		// Defining which cells are in the shortest path tree
		outLayer.clearWeights();
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				outLayer.trace(i * nCols + j);
			}
		}
		return outLayer;
	}
	
	public Layer dijkstra(String outLayerName, int startIndex, int endIndex) {
		Layer pathLayer = dijkstra(outLayerName, startIndex);
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				outLayer.values[i][j] = values[i][j];
			}
		}
		
		outLayer.pathValue = outLayer.getMin() - 1;
		if (outLayer.pathValue == outLayer.nullValue) {
			outLayer.pathValue -= 1;
		}
		
		int row = endIndex / nCols;
		int col = endIndex % nCols;
		int prevIndex = pathLayer.prev[row][col];
		
		while (prevIndex != -1) {
			outLayer.values[row][col] = outLayer.pathValue;
			startIndex = prevIndex;
			row = startIndex / nCols;
			col = startIndex % nCols;
			prevIndex = pathLayer.prev[row][col];
		}
		outLayer.values[row][col] = outLayer.pathValue;
		
		return outLayer;
	}
	
	private void clearWeights() {
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				values[i][j] = 0;
			}
		}
	}
	
	private void trace(int index) {
		int row = index / nCols;
		int col = index % nCols;
		int prevIndex = prev[row][col];
		
		while (prevIndex != -1) {
			values[row][col] = 1;
			index = prevIndex;
			row = index / nCols;
			col = index % nCols;
			prevIndex = prev[row][col];
		}
	}
	
	// To be continued
	public void dijkstraLearning() {
		
	}
	
	// Helper methods and misc
	//return the number of unique values in a double array
		private int getNumUnique(double[] input) {
			//get the number of unique values in an array.
			Set<Double> set = new HashSet<Double>();
			for(int i = 0; i < input.length;i++) {
				set.add(input[i]);
			}
			return set.size();
		}
	
	private double getMax() {
		double maxNum = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				if (values[i][j] > maxNum && values[i][j] != nullValue && values[i][j] != pathValue) {
					maxNum = values[i][j];
				}
			}
		}
		return maxNum;
	}
	
	private double getMin() {
		double minNum = Double.POSITIVE_INFINITY;
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				if (values[i][j] < minNum && values[i][j] != nullValue && values[i][j] != pathValue) {
					minNum = values[i][j];
				}
			}
		}
		return minNum;
	}
	
	// Used for visualizing multiple images of different scales in the same MapPanel
	private double rescale(double value, double oldMin, double oldMax, double newMin, double newMax) {
		double outValue = oldMin + (oldMax - oldMin) * (value - newMin) / (newMax - newMin); // Formula: b = b0 + (b1 - b0) * (a - a0) / (a1 - a0)
		if (newMax - newMin == 0) {
			outValue = newMax;
		} else if (oldMax - oldMin == 0) {
			outValue = newMin;
		}
		return outValue;
	}

	// Used to slow down animations for raster3x4.txt
	public String getFileName(String path) {
		String[] tokens = path.split("/");
		int i = tokens.length - 1;
		String fileName = tokens[i];
		return fileName;
	}
	
	// To test the getNeighborhood method
	public Layer neighborhoodSize(int r, boolean isSquare, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				int input = i*nCols + j;
				int[][] neighborhood = outLayer.getNeighborhood(input, r, isSquare);

				int counter = 0;
				for (int[] neighbor: neighborhood) {
					counter++;
				}
				outLayer.values[i][j] = counter;
			}
		}
		return outLayer;
	}

	private int[][] getNeighborhood(int i, int r, boolean isSquare) {
		int row = i / nCols;
		int col = i % nCols;
		int startRow = Math.max(row - r,0);
		int endRow = Math.min(row + r, nRows-1);
		int startCol = Math.max(col - r,0);
		int endCol = Math.min(col + r, nCols-1);
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		// Square neighborhoods
		if (isSquare == true) {
			for (int j = startRow; j <= endRow; j++) {
				for (int k = startCol; k <= endCol; k++) {
						Integer indexObj = j * nCols + k;
						list.add(indexObj);
				}
			}
		}
		
		// Circular neighborhoods
		else {
			for (int j = startRow; j <= endRow; j++) {
				for (int k = startCol; k <= endCol; k++) {
					double d = Math.sqrt( Math.pow(j - row, 2) + Math.pow(k - col, 2) );
					// Add distance check as a condition
					if (d <= r) {
						Integer indexObj = j * nCols + k;
						list.add(indexObj);
					}
				}
			}
		}
		
		// List to array
		int[][] outputPos = new int[list.size()][2];
		int counter = 0;
		for (Integer indexObj: list) {
			outputPos[counter][0] = indexObj / nCols;;
			outputPos[counter][1] = indexObj % nCols;
			counter++;
		}
		
		return outputPos;
	}
	
	// Learning visualizations
	public void localSumLearningTest(
			Layer inLayer, String outLayerName, 
			int scale, double visitValue,
			String path, JLayeredPane layeredPane) throws InterruptedException {
//		JFrame appFrame = new JFrame();
//		Dimension dimension = new Dimension(800, 500);
//		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		TestGUI.getMapStartX();
		TestGUI.getMapStartY();
		
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		Layer sumLayer = localSum(inLayer, outLayerName);
		
		String fileName = getFileName(path);
		
		double maxNum = sumLayer.getMax();
		double minNum = sumLayer.getMin();
		
		if (TestGUI.mPanel != null) {
			layeredPane.remove(TestGUI.mPanel);
			layeredPane.revalidate();
			layeredPane.repaint();
		}
		
		BufferedImage image = outLayer.toImage();
		MapPanel map = new MapPanel(image, scale);
		layeredPane.add(map);
		map.setBounds(TestGUI.mapStartX, TestGUI.mapStartY, 2000, 2000);	
		map.setExtendedState(JFrame.MAXIMIZED_BOTH);
		layeredPane.revalidate();
		layeredPane.repaint();
//		map.setPreferredSize(dimension);
//		appFrame.pack();
//		appFrame.setVisible(true);
		
		for(int i = 0; i < nRows; i++){
			for (int j = 0; j < nCols; j++) {
				// Slow down to visualize animation for smaller layer
                if (fileName.equals("raster3x4.txt")) {
                	Thread.sleep(200);
                }
                
				outLayer.values[i][j] = visitValue;
				
				layeredPane.remove(map);
				layeredPane.revalidate();
				layeredPane.repaint();
				
				BufferedImage image2 = outLayer.toImageLearning(maxNum, minNum, visitValue, visitValue - 10000);
				MapPanel map2 = new MapPanel(image2, scale);
				layeredPane.add(map2);
				map2.setBounds(TestGUI.mapStartX, TestGUI.mapStartY, 2000, 2000);	
				map2.setExtendedState(JFrame.MAXIMIZED_BOTH);
				layeredPane.revalidate();
				layeredPane.repaint();
//				appFrame.setVisible(true);
				map = map2;
				
				outLayer.values[i][j] = values[i][j] + inLayer.values[i][j];
			}
		}
		
		// Slow down to visualize animation for smaller layer
        if (fileName.equals("raster3x4.txt")) {
        	Thread.sleep(200);
        }

		layeredPane.remove(TestGUI.mPanel);
		layeredPane.revalidate();
		layeredPane.repaint();
        
		BufferedImage image2 = outLayer.toImageLearning(maxNum, minNum, visitValue, visitValue - 10000);
		MapPanel map2 = new MapPanel(image2, scale);
		layeredPane.add(map2);
		map2.setBounds(TestGUI.mapStartX, TestGUI.mapStartY, 2000, 2000);	
		map2.setExtendedState(JFrame.MAXIMIZED_BOTH);
		layeredPane.revalidate();
		layeredPane.repaint();
//		appFrame.setVisible(true);
//		map = map2;
	}
	
	public void localSumLearning(Layer inLayer, String outLayerName, int scale, double visitValue, String path) throws InterruptedException {
		JFrame appFrame = new JFrame();
		Dimension dimension = new Dimension(800, 500);
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		Layer sumLayer = localSum(inLayer, outLayerName);
		
		String fileName = getFileName(path);
		
		double maxNum = sumLayer.getMax();
		double minNum = sumLayer.getMin();
		
		BufferedImage image = outLayer.toImage();
		MapPanel map = new MapPanel(image, scale);
		appFrame.add(map);
		map.setPreferredSize(dimension);
		appFrame.pack();
		appFrame.setVisible(true);
		
		for(int i = 0; i < nRows; i++){
			for (int j = 0; j < nCols; j++) {
				// Slow down to visualize animation for smaller layer
                if (fileName.equals("raster3x4.txt")) {
                	Thread.sleep(200);
                }
                
				outLayer.values[i][j] = visitValue;
				BufferedImage image2 = outLayer.toImageLearning(maxNum, minNum, visitValue, visitValue - 10000);
				MapPanel map2 = new MapPanel(image2, scale);
				appFrame.remove(map);
				appFrame.add(map2);
				appFrame.setVisible(true);
				map = map2;
				
				outLayer.values[i][j] = values[i][j] + inLayer.values[i][j];
			}
		}
		
		// Slow down to visualize animation for smaller layer
        if (fileName.equals("raster3x4.txt")) {
        	Thread.sleep(200);
        }
		BufferedImage image2 = outLayer.toImageLearning(maxNum, minNum, visitValue, visitValue - 10000);
		MapPanel map2 = new MapPanel(image2, scale);
		appFrame.remove(map);
		appFrame.add(map2);
		appFrame.setVisible(true);
		map = map2;
	}
	
	public void focalVarietyLearning(int r, boolean isSquare, String outLayerName, int scale, double visitValue, String path) throws InterruptedException {
		JFrame appFrame = new JFrame();
		Dimension dimension = new Dimension(800, 500);
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String fileName = getFileName(path);
		
		double maxNum = getMax();
		double minNum = getMin();
		double highlightValue = visitValue - 10000;
		
		// To keep "real" values, one layer is used for calculations, and one is used for printing
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		Layer printLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		
		// Initialize printLayer values
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				printLayer.values[i][j] = values[i][j];
			}
		}
		
		// For calculations
		Layer varietyLayer = focalVariety(r, isSquare, outLayerName);
		double varMax = varietyLayer.getMax();
		double varMin = varietyLayer.getMin();
		
		// Initial map, to avoid empty JFrame
		BufferedImage image = toImage();
		MapPanel map = new MapPanel(image, scale);
		appFrame.add(map);
		map.setPreferredSize(dimension);
		appFrame.pack();
		appFrame.setVisible(true);
		
        for(int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
            	// Only update relevant rows and columns to make it faster
            	int startRow = Math.max(i - (r + 1), 0);
            	int endRow = Math.min(i + (r + 1), nRows);
            	int startCol = Math.max(j - (r + 1), 0);
            	
            	// Up until current column
                for (int k = startRow; k < i; k++) {
                	for (int l = startCol; l < nCols; l++) {
                		printLayer.values[k][l] = outLayer.values[k][l];
                	}
                }
                
                // Beginning of the current column
                for (int l = 0; l <= j; l++) {
					printLayer.values[i][l] = outLayer.values[i][l];
				}
                
                // The rest of the current column
				for (int l = j + 1; l < nCols; l++) {
					printLayer.values[i][l] = values[i][l];
				}
				
				// The rest of the columns
				for (int k = i + 1; k < endRow; k++) {
					for (int l = startCol; l < nCols; l++) {
						printLayer.values[k][l] = values[k][l];
					}
				}
                
				int input = i * nCols + j;
                int[][] neighborhood = outLayer.getNeighborhood(input, r, isSquare);
                
                for (int[] neighbor: neighborhood) {
                	int row = neighbor[0];
                    int col = neighbor[1];
                    printLayer.values[row][col] = visitValue;
                }
				
                printLayer.values[i][j] = highlightValue;
                
                // Slow down to visualize animation for smaller layer
                if (fileName.equals("raster3x4.txt")) {
                	Thread.sleep(200);
                }
                
				BufferedImage image2 = printLayer.toImageLearning(maxNum, minNum, visitValue, highlightValue);
				MapPanel map2 = new MapPanel(image2, scale);
				appFrame.remove(map);
				appFrame.add(map2);
				appFrame.setVisible(true);
				map = map2;
				
				printLayer.values[i][j] = values[i][j];
                
                // Create list of unique values for specified neighborhood
                ArrayList<Double> list = new ArrayList<Double>();
                for (int[] neighbor: neighborhood) {
                    int row = neighbor[0];
                    int col = neighbor[1];
                    
                    Double valueObj = values[row][col];
                    
                    if (!list.contains(valueObj)) {
                        list.add(valueObj);
                    }
                }
                outLayer.values[i][j] = rescale(list.size(), minNum, maxNum, varMin, varMax);
            }
        }
        
        // Slow down to visualize animation for smaller layer
        if (fileName.equals("raster3x4.txt")) {
        	Thread.sleep(200);
        }
        
		BufferedImage image2 = varietyLayer.toImage(); // Learning(maxNum, minNum, visitValue, visitValue - 10000);
		MapPanel map2 = new MapPanel(image2, scale);
		appFrame.remove(map);
		appFrame.add(map2);
		appFrame.setVisible(true);
		map = map2;
	}
	
	public void zonalMinimumLearning(Layer zoneLayer, String outLayerName, int scale, double visitValue, double highlightValue, String path) throws InterruptedException {
		JFrame appFrame = new JFrame();
		Dimension dimension = new Dimension(800, 500);
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String fileName = getFileName(path);
		
		double maxNum = getMax();
		double minNum = getMin();
		
		// To keep "real" values, one layer is used for calculations, and one is used for printing
		Layer outLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		Layer printLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		Layer zonePrintLayer = new Layer(outLayerName, nRows, nCols, origin, resolution, nullValue);
		
		BufferedImage image = toImage();
		MapPanel map = new MapPanel(image, scale);
		appFrame.add(map);
		map.setPreferredSize(dimension);
		appFrame.pack();
		appFrame.setVisible(true);
		
		// Hashmap for changing lowest value visualization
		HashMap<Double, Integer> hmVis = new HashMap<Double, Integer>();
		
		// Create hashmap with associated lowest values
		HashMap<Double, Double> hm = new HashMap<Double, Double>();
		for(int i = 0; i < nRows; i++){
			for (int j = 0; j < nCols; j++) {
				
				// The rest of the current column
				for (int l = j; l < nCols; l++) {
					printLayer.values[i][l] = values[i][l];
				}
				
				// The rest of the columns
				for (int k = i + 1; k < nRows; k++) {
					for (int l = 0; l < nCols; l++) {
						printLayer.values[k][l] = values[k][l];
					}
				}
                // Temporary cursor
                printLayer.values[i][j] = visitValue;
                
                // Slow down to visualize animation for smaller layer
                if (fileName.equals("raster3x4.txt")) {
                	Thread.sleep(200);
                }
                
				BufferedImage image2 = printLayer.toImageLearning(maxNum, minNum, visitValue, highlightValue);
				MapPanel map2 = new MapPanel(image2, scale);
				appFrame.remove(map);
				appFrame.add(map2);
				appFrame.setVisible(true);
				map = map2;
				// Update current cell to its real value after image is created
				printLayer.values[i][j] = values[i][j];

				Double key = zoneLayer.values[i][j];
				Double value = values[i][j];
				if (!hm.containsKey(key) || hm.get(key) > value) {
					// Check if previous minimum value exists, and change its highlight status to none
					if (hmVis.get(key) != null) {
						int prev = hmVis.get(key);
						int prevRow = prev / nCols;
						int prevCol = prev % nCols;
						printLayer.values[prevRow][prevCol] = values[prevRow][prevCol];
					}
					hmVis.put(key, i * nCols + j);
					hm.put(key,  value);
					printLayer.values[i][j] = highlightValue;
				}
			}
		}
		
		// Slow down to visualize animation for smaller layer
        if (fileName.equals("raster3x4.txt")) {
        	Thread.sleep(200);
        }
        
		BufferedImage image2 = printLayer.toImageLearning(maxNum, minNum, visitValue, highlightValue);
		MapPanel map2 = new MapPanel(image2, scale);
		appFrame.remove(map);
		appFrame.add(map2);
		appFrame.setVisible(true);
		map = map2;
		
		// Assign lowest values to output layer
		for(int i = 0; i < nRows; i++){
			for (int j = 0; j < nCols; j++) {
				Double key = zoneLayer.values[i][j];
				outLayer.values[i][j] = hm.get(key);
				
				// The rest of the current column
				for (int l = j; l < nCols; l++) {
					zonePrintLayer.values[i][l] = printLayer.values[i][l];
				}
				
				// The rest of the columns
				for (int k = i + 1; k < nRows; k++) {
					for (int l = 0; l < nCols; l++) {
						zonePrintLayer.values[k][l] = printLayer.values[k][l];
					}
				}
				
				// Temporary cursor
                zonePrintLayer.values[i][j] = visitValue;
                
                // Slow down to visualize animation for smaller layer
                if (fileName.equals("raster3x4.txt")) {
                	Thread.sleep(200);
                }
                
				image2 = zonePrintLayer.toImageLearning(maxNum, minNum, visitValue, highlightValue);
				map2 = new MapPanel(image2, scale);
				appFrame.remove(map);
				appFrame.add(map2);
				appFrame.setVisible(true);
				map = map2;
				
				// Update current cell to its real value
				zonePrintLayer.values[i][j] = outLayer.values[i][j];
			}
		}
		
		// Slow down to visualize animation for smaller layer
        if (fileName.equals("raster3x4.txt")) {
        	Thread.sleep(200);
        }
        
		image2 = zonePrintLayer.toImageLearning(maxNum, minNum, visitValue, highlightValue);
		map2 = new MapPanel(image2, scale);
		appFrame.remove(map);
		appFrame.add(map2);
		appFrame.setVisible(true);
		map = map2;
	}
	
}