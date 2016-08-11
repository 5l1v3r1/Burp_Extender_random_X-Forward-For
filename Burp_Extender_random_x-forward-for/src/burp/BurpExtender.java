package burp;

import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;
import java.net.URLEncoder;


public class BurpExtender implements IBurpExtender, IHttpListener
{
    private IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;
    private PrintWriter stdout;//�������ﶨ�����������registerExtenderCallbacks������ʵ������������ں����о�ֻ�Ǿֲ���������������ʵ��������ΪҪ�õ�����������
    
    // implement IBurpExtender
    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
    	stdout = new PrintWriter(callbacks.getStdout(), true);
    	//PrintWriter stdout = new PrintWriter(callbacks.getStdout(), true); ����д���Ƕ��������ʵ����������ı��������µı���������֮ǰclass�е�ȫ�ֱ����ˡ�
    	//stdout.println("testxx");
    	//System.out.println("test"); ���������burp��
        this.callbacks = callbacks;
        helpers = callbacks.getHelpers();
        callbacks.setExtensionName("Random_X-forward-For"); //�������
        callbacks.registerHttpListener(this); //���û��ע�ᣬ�����processHttpMessage�����ǲ�����Ч�ġ������������Ӧ���Ĳ�������Ӧ���Ǳ�Ҫ��
    }

    @Override
    public void processHttpMessage(int toolFlag,boolean messageIsRequest,IHttpRequestResponse messageInfo)
    {
    	try{
	    	if (toolFlag == 64 || toolFlag == 16 || toolFlag == 32 || toolFlag == 4){ //��ͬ��toolflag�����˲�ͬ��burp��� https://portswigger.net/burp/extender/api/constant-values.html#burp.IBurpExtenderCallbacks
	    		if (messageIsRequest){ //����������д���
	    			IRequestInfo analyzeRequest = helpers.analyzeRequest(messageInfo); //����Ϣ����н��� 
	    			String request = new String(messageInfo.getRequest());
	    			byte[] body = request.substring(analyzeRequest.getBodyOffset()).getBytes();
	    			List<String> headers = analyzeRequest.getHeaders(); //��ȡhttp����ͷ����Ϣ�����ؿ��Կ�����һ��python�е��б�java���ǽз���ʲô�ģ���ûŪ���
	    			String xforward = "X-Forwarded-For: "+RandomIP.RandomIPstr();
	    			headers.add(xforward);
	    			stdout.println(xforward);
	    			byte[] new_Request = helpers.buildHttpMessage(headers,body);
	    			stdout.println(helpers.analyzeRequest(new_Request).getHeaders());
	    			messageInfo.setRequest(new_Request);//���������µ������
	    		}	    		
	    	}
    	}
    	catch(Exception e){
    		stdout.println(e);
    	}
    		
    }
}