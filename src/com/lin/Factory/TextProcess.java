package com.lin.Factory;

public class TextProcess {
	
	private static final String replyHeader = "\n\n【 在 %s 的大作中提到: 】\n";
	
	public static String wrapRepliedText(String content, String userId, int maxRow) {
		String wrappedText = "";
		String[] lines = content.split("\n");
		int lineCount = lines.length < maxRow ? lines.length : maxRow;
		for(int i = 0; i < lineCount; i ++) {
			wrappedText += ": " + lines[i] + "\n";
		}
		if(lines.length > maxRow) {
			wrappedText += ": .......................\n";
		}
		return String.format(replyHeader, userId) + wrappedText;
	}
}
