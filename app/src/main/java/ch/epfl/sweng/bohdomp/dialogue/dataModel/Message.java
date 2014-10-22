package ch.epfl.sweng.bohdomp.dialogue.dataModel;

import android.telephony.SmsMessage;

/**
 * Class representing a Dialogue message
 */
public class Message {

    //FIXME: full implementation is needed
    public static Message convertFromSmsMessage(SmsMessage smsMessage) {
        return new Message(smsMessage.getMessageBody(), null, new Conversation(), new Contact());
    }

    public static SmsMessage convertToSmsMessage(Message message) {
        return null;
    }
    private final String body;
    private final String destinationAddress;
    private final Conversation conversation;
    private final Contact sender;


    public Message(String bodyParameter, String destinationAddressParameter,
                   Conversation converstationParameter, Contact senderParameter
                   ) {
        if (constructorArgumentsOk(bodyParameter, destinationAddressParameter,
                converstationParameter, senderParameter)) {
            this.body = bodyParameter;
            this.conversation = converstationParameter;
            this.destinationAddress = destinationAddressParameter;
            this.sender = senderParameter;
        } else {
            throw new IllegalArgumentException("Invalid parameters for message creation");
        }
    }
    //FIXME: add geters
    public String getBody() {
        return body;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    private boolean constructorArgumentsOk(String bodyParameter, String destinationAddressParameter,
                                           Conversation converstationParameter, Contact senderParameter
    ) {
        return bodyParameter != null &&  converstationParameter != null
                && (senderParameter!=null ^ destinationAddressParameter!=null);
    }




}
