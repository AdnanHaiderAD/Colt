package pt.tumba.spell;

import java.util.List;
import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
/* Author : Adnan :- this class is writtern to investigate how break iterators work*/
public class BreakerTest {

	public static void main(String args[]) {
		
	      if (args.length >0) {
	          String stringToExamine = "hahah this is me check's must be done:ok";
	          //print each word in order
	          BreakIterator boundary = BreakIterator.getWordInstance();
	          boundary.setText(stringToExamine);
	          System.out.println("the first is "+ boundary.first()+ "the last is "+boundary.last());
	          printEachForward(boundary, stringToExamine);
	          //print each sentence in reverse order
	          boundary = BreakIterator.getSentenceInstance(Locale.UK);
	          boundary.setText(stringToExamine);
	          System.out.println("the first is "+ boundary.first()+ "the last is "+boundary.last());
	          printEachBackward(boundary, stringToExamine);
	         // printFirst(boundary, stringToExamine);
	         // printLast(boundary, stringToExamine);
	          Map map =new HashMap();
	          map.put("hello", "word");
	           List l= (List) (map.get("hllo"));
	          if(l == null){
	        	l = (List) new Vector();  
	          }
	          l.add("word");
	          System.out.println(l.toString());
	       
	      }
	 }
	 public static void printEachForward(BreakIterator boundary, String source) {
	     int start = boundary.first();
	     for (int end = boundary.next();end != BreakIterator.DONE; start = end, end = boundary.next()) {
	          System.out.println(source.substring(start,end));
	     }
	 }	
	 public static void printEachBackward(BreakIterator boundary, String source) {
	     int end = boundary.last();
	     for (int start = boundary.previous();start != BreakIterator.DONE; end = start, start = boundary.previous()) {
	
	         System.out.println(source.substring(start,end));
	     }
	 }
	 public static void printFirst(BreakIterator boundary, String source) {
	     int start = boundary.first();
	     int end = boundary.next();
	     System.out.println(source.substring(start,end));
	 }
	 public static void printLast(BreakIterator boundary, String source) {
	     int end = boundary.last();
	     int start = boundary.previous();
	     System.out.println(source.substring(start,end));
	 }
	 public static void printAt(BreakIterator boundary, int pos, String source) {
	     int end = boundary.following(pos);
	     int start = boundary.previous();
	     System.out.println(source.substring(start,end));
	 }
	 public static int nextWordStartAfter(int pos, String text) {
	     BreakIterator wb = BreakIterator.getWordInstance();
	     wb.setText(text);
	     int last = wb.following(pos);
	     int current = wb.next();
	     while (current != BreakIterator.DONE) {
	         for (int p = last; p < current; p++) {
	             if (Character.isLetter(text.codePointAt(p)))
	                 return last;
	         }
	         last = current;
	         current = wb.next();
	     }
	     return BreakIterator.DONE;
	 }
	 
}
