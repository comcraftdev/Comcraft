package net.comcraft.src;

public class ComcraftException extends RuntimeException {

    private String originalMessage;
    private String originalClass;

    public ComcraftException(Throwable ex) {
        this("", ex);
    }
    
    public ComcraftException(String info, Throwable ex) {
        super(info);
        
        if (ex != null) {
            originalMessage = ex.getMessage();
            originalClass = ex.getClass().getName();
        } else {
            originalMessage = "";
            originalClass = "";
        }
    }

    public String getOriginalMessage() {
        return originalMessage;
    }
    
    public String getOriginalClassName() {
        return originalClass;
    }
    
}
