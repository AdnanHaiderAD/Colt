package pt.tumba.spell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

/*author:Adnan 
 * This class reads texts and filters badly recognised words by the OCR
 */
public class FilterText {

	private TernarySearchTrie dictionary;
	private DefaultWordFinder finder;
	private HashSet<String> dict;
	public FilterText(){
		try {
			this.dictionary =new TernarySearchTrie(new File("/usr/share/dict/british-english"));
		    this.finder = new DefaultWordFinder();
		    storeData(new File("/usr/share/dict/british-english"));
		} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("Error file doesnt exist");
		}
	}
	/* reads a line and removes words in the line which are not in the dictionary*/
	public String filterText(String text){
		finder.setText(text);
		String word;
		while((word=finder.next())!=null){
			///Integer index = this.dictionary.getAndIncrement(word);
			String dictWord= (String) this.dictionary.get(word);
			if (dictWord==null) finder.replace("");
			
			//if(!dict.contains(word)) finder.replace("");
		}
		return finder.getText();
	}
	/* Stores the dictionary in an hash set which is an alternative data structure  to TST*/
	public void storeData(File file) throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String word;
		dict = new HashSet<String>();
		while((word=in.readLine())!=null){
			dict.add(StringUtils.toLowerCase(word.trim(), false));
		}
		in.close();
	}
	public static void main(String[] args) throws IOException{
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		FilterText filter= new FilterText();
		System.out.println("Please type in a text:");
		
		 ///String input  = FileUtils.readFileToString(new File("/home/adnan/Documents/A - PLEADINGS/A2 - REQUEST FOR FURTHER INFORMATION/0011 A2 - Tab 15 - Response to Outstanding Part 18 Request fo Further Information.txt"));
		 long start = System.currentTimeMillis();
		// String output = filter.filterText(input);
		 //System.out.print(output);
		 long end = System.currentTimeMillis();
		 //System.out.println("the size is "+MemoryUtil.deepMemoryUsageOf(filter.dictionary.rootNode));
		 System.out.println("the run time is "+ (end-start));
		
		
	}
	
	
	
}
