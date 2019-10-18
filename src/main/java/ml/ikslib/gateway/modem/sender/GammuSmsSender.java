package ml.ikslib.gateway.modem.sender;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DaemonExecutor;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ml.ikslib.gateway.modem.client.common.SmsMessage;

public class GammuSmsSender implements SmsSender {

    private final static Logger LOG =LogManager.getLogger(GammuSmsSender.class.getName());
    
    private final PumpStreamHandler executeOutputLogger = new PumpStreamHandler(new LogOutputStream(Level.INFO.intLevel()) {
        @Override
        protected void processLine(String line, int logLevel) {
            LOG.log(Level.WARN, line);
        }
    });

    @Override
    public void send(SmsMessage smsMessage) {
        try {
            sendSmsUsingGammuCommandLineTool(smsMessage);
        } catch (Exception e) {
            LOG.error("Failed execute a command: " + e.getMessage(), e);
        }
    }

    private void sendSmsUsingGammuCommandLineTool(SmsMessage smsMessage) throws IOException, InterruptedException {
        LOG.info("Sending SMS message...");
        CommandLine command = makeCommand(smsMessage);
        LOG.info(command);

        DefaultExecuteResultHandler result = executeCommand(command);

        ExecuteException exception = result.getException();
        if (exception != null) {
            LOG.error("Failure of sending SMS message:" + exception.getMessage(), exception);
        }else if (result.getExitValue() == 0) {
            LOG.info("SMS message sent");
        }else {
            LOG.error("SMS message sending failed, program returns exit code: " + result.getExitValue());
        }
    }

    private DefaultExecuteResultHandler executeCommand(CommandLine command) throws IOException, InterruptedException {
        DaemonExecutor executor = new DaemonExecutor();
        executor.setStreamHandler(executeOutputLogger);
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        executor.execute(command, resultHandler);
        resultHandler.waitFor();
        return resultHandler;
    }

    protected CommandLine makeCommand(SmsMessage smsMessage) {

        CommandLine commandLine = new CommandLine("gammu");
        commandLine.addArgument("sendsms") ;
        commandLine.addArgument("TEXT") ;
        commandLine.addArgument(smsMessage.getRecipient()) ;
        commandLine.addArgument("-text") ;
        commandLine.addArgument(smsMessage.getText());
        return commandLine;
    }

    @Override
    public String toString() {
        return "GammuSmsSender{}";
    }
}
