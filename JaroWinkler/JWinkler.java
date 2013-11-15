import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JWinkler {
	
	public static void main(String[] args) {
		
		JWinkler jw = new JWinkler();
		
		
		float fAns = jw.getJaroWinklerMetric("hellolo", "helolol");
		
		System.out.println(fAns);
		
	}
	
	public float getJaroWinklerMetric(String string1, String string2) {

                
        List<String> lstCommonChar1 = getCommonCharacters(string1, string2);
        List<String> lstCommonChar2 = getCommonCharacters(string2, string1);

        //check for zero in common
        if (lstCommonChar1.size() == 0 || lstCommonChar2.size() == 0 || lstCommonChar1.size() != lstCommonChar2.size()) {
            return 0.0f;
        }

        //get the number of transpositions
        int transpositions = 0;
        int n = lstCommonChar1.size();
        for (int i = 0; i < n; i++) {
            if (!lstCommonChar1.get(i).equals(lstCommonChar2.get(i)))
                transpositions++;
        }
        transpositions /= 2.0f;

        //calculate jaro metric
        return (n / ((float) string1.length()) +
                n / ((float) string2.length()) +
                (n - transpositions) / ((float) n)) / 3.0f;
    }

	private static List<String> getCommonCharacters(String string1, String string2) {
        
        List<String> lstReturnCommons = new ArrayList<String>();
        
        HashMap<String, Integer> unique = new HashMap<String, Integer>();
        
        char[] char1 = string1.toCharArray();
        char[] char2 = string2.toCharArray();
        
        boolean bCheck;
        String subString;
        
        for(int i = 0; i < char1.length; i++)
        {
        	bCheck = true;
        	for(int j = 0; j < char2.length && bCheck; j++)
        	{
        		if(unique.containsKey(char1[i] + "")){
        			bCheck = false;
        			int numberofchecks = unique.get(char1[i] + "");
        			int start = 0;
        			subString = string2;
        			for(int z = 0; z < numberofchecks; z++){
        				start = subString.indexOf(char1[i]);
        				subString = subString.substring(start + 1);
        			}
        		
        			if(subString.contains(char1[i] + "")){
        				unique.put(char1[i] + "", (unique.get(char1[i] + "") + 1));
        				lstReturnCommons.add(char1[i] + "");
        			}        			
        		}
        		else if(char1[i] == char2[j]){
        			bCheck = false;
        			unique.put(char1[i] + "", 1);
        			lstReturnCommons.add(char1[i] + "");
        		}
        	}
        }      
        
        return lstReturnCommons;
    }

    
}
