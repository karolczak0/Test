package kao.monkey.expressions;

/** This is 'template' of every Expression. Each Expression is accepted by some ExpressionDefinition.
 * @author k.antczak */
public class ExpressionDefinition{
	
	/** Definition for root expression */
	public static final ExpressionDefinition ROOT_DEFINITION = new ExpressionDefinition("ROOT", null, null, null);
	/** Definition for file expression */
	public static final ExpressionDefinition FILE_DEFINITION = new ExpressionDefinition("FILE", null, null, null);
	/** Definition for directory expression */
	public static final ExpressionDefinition DIRECTORY_DEFINITION = new ExpressionDefinition("DIR", null, null, null);
	
	String inputToken, outputToken, regex, name;
		
	public ExpressionDefinition(String name, String regex, String inputToken, String outputToken) {
		this.name = name;
		this.regex = regex;
		this.inputToken = inputToken;
		this.outputToken = outputToken;
	}
	
	/** Checks if text contains input token for expression. 
	 * If so, returns index of token. Otherwise returns -1 */
	public int isInput(String text) {
		return getInputToken()!=null ? text.indexOf(getInputToken()) : -1;
	}

	/** Checks if text contains output token for expression. 
	 * If so, returns index of token. Otherwise returns -1 */
	public int isOutput(String text) {
		return getOutputToken() != null ? text.indexOf(getOutputToken()) : -1;
	}
	
	/** Returns true if given text was successfully parsed or false otherwise. */
	public boolean tryParse(String text) {
		return getRegex() != null ? text.matches(getRegex()) : false;
	}
	
	/** Returns token which starts scope of expression */
	public String getInputToken() {
		return inputToken;
	}

	/** Returns token which ends scope of expression */
	public String getOutputToken() {
		return outputToken;
	}

	/** Returns regular expression, which accepts expression */
	public String getRegex() {
		return regex;
	}

	/** Returns type of expression definition */
	public String getName() {
		return name;
	}
	
	public boolean equals(Object o) {
		 if (o == null) return false;
		    if (o == this) return true;
		    if (!(o instanceof ExpressionDefinition))return false;
		    ExpressionDefinition e = (ExpressionDefinition)o;

		return getName().equals(e.getName()) && getRegex().equals(e.getRegex()) && 
				getInputToken().endsWith(e.getInputToken()) && getOutputToken().equals(e.getOutputToken());
	}

}
