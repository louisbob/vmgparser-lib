package net.owl_black.tovmgr;

/**
 * SMS object. Allow to store all informations extracted from VMG files
 * 
 * @author (Louisbob) 
 * @version (v1.0 - 9/4/2012)
 */
public class mySms
{
        //Object that stores informations about SMSs
        
        private String _phoneNumber;
        private String _senderName;
        private int _dateSent[] = new int[3];     //nÂ° day, month and year (French style)
        private int _timeSent[] = new int[3];     //hours, minutes, seconds
        private String _textMsg;                  //Content of the SMS
        private int _encode = 0;

        private Boolean isReceived;  //Indicate if inbox either outbox
        
        //Constant for the _encode option:
    	final static int UTF8_DEFAULT = 0;
        final static int UTF8_CLASSICAL = 1;
    	final static int UTF8_ARABIC = 2;
        
        /********************************************************************************/
            /* Mutateur */
        public void setPhoneNum(String pNum) {
            _phoneNumber = pNum;
        }
        
        public void setSenderName(String sName) {
            _senderName = sName;
        }
        
        //Date
        public void setDate(int dNum, int mNum, int yNum) {
            _dateSent[0] = dNum;
            _dateSent[1] = mNum;
            _dateSent[2] = yNum;
        }
        
        public void setDay(int dNum) {
            _dateSent[0] = dNum;
        }
        
        public void setMonth(int mNum) {
            _dateSent[1] = mNum;
        }
        
        public void setYear(int yNum) {
            _dateSent[2] = yNum;
        }
        
        //Time
        public void setTime(int hNum, int mNum, int sNum) {
            _timeSent[0] = hNum;
            _timeSent[1] = mNum;
            _timeSent[2] = sNum;
        }
        
        public void setHour(int hNum) {
            _timeSent[0] = hNum;
        }
        
        public void setMin(int mNum) {
            _timeSent[1] = mNum;
        }
        
        public void setSec(int sNum) {
            _timeSent[2] = sNum;
        }
        
        //MessageText
        public void setText(String text){
            _textMsg = text;
        }
        
        //Inbox or Outbot
        public void setReceived() {
            isReceived = true;
        }
        
        public void setSent() {
            isReceived = false;
        }
        
        public void setEncode(int enc) {
        	_encode = enc;
        }
        
        /********************************************************************************/
            /* Accesseur */
            
        public String getPhoneNum() {
            return _phoneNumber;
        }
        
        public String getSenderName() {
            return _senderName;
        }
        
        
        //Date
        public int getDay() {
            return _dateSent[0];
        }
        
        public int getMonth() {
            return _dateSent[1];
        }
        
        public int getYear() {
            return _dateSent[2];
        }
        
        //Time
        public int getHour() {
            return _timeSent[0];
        }
        
        public int getMin() {
            return _timeSent[1];
        }
        
        public int getSec() {
            return _timeSent[2];
        }
        
        //MessageText
        public String getText(){
            return _textMsg;
        }
        
        //Inbox or Outbot
        public Boolean getReceived() {
            return isReceived;
        }
        
        public int getEncode() {
        	return _encode;
        }
        
        static public String replaceNonUTF8(String myString)
        {
            String line = myString;

                    line = line.replaceAll("=20", " ");    //remplacement des espaces
                    line = line.replaceAll("=0D=0A", "\n");//Retour chariot
                    line = line.replaceAll("=C3=AA", "ê");
                    line = line.replaceAll("=C3=A9", "é");
                    line = line.replaceAll("=C3=A8", "è");
                    line = line.replaceAll("=C3=A7", "ç");
                    line = line.replaceAll("=C3=A0", "ç");
                    line = line.replaceAll("=C3=A0", "à");
                    line = line.replaceAll("=C3=BB", "û");
                    return line;
        }
}
