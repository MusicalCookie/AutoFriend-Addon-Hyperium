package pw._2pi.autofriend;

public class ChatUtils
{
    private final eu parent;
    private String text;
    private ez style;
    
    private ChatUtils(final String text) {
        this(text, null, Inheritance.SHALLOW);
    }
    
    private ChatUtils(final String text, final eu parent, final Inheritance inheritance) {
        this.parent = parent;
        this.text = text;
        switch (inheritance) {
            case DEEP: {
                this.style = ((parent != null) ? parent.b() : new ez());
                break;
            }
            default: {
                this.style = new ez();
                break;
            }
            case NONE: {
                this.style = new ez().a((a)null).a(false).b(false).c(false).d(false).e(false).a((et)null).a((ew)null).a((String)null);
                break;
            }
        }
    }
    
    public static ChatUtils of(final String text) {
        return new ChatUtils(text);
    }
    
    public ChatUtils setColor(final a color) {
        this.style.a(color);
        return this;
    }
    
    public ChatUtils setBold(final boolean bold) {
        this.style.a(bold);
        return this;
    }
    
    public ChatUtils setItalic(final boolean italic) {
        this.style.b(italic);
        return this;
    }
    
    public ChatUtils setStrikethrough(final boolean strikethrough) {
        this.style.c(strikethrough);
        return this;
    }
    
    public ChatUtils setUnderlined(final boolean underlined) {
        this.style.d(underlined);
        return this;
    }
    
    public ChatUtils setObfuscated(final boolean obfuscated) {
        this.style.e(obfuscated);
        return this;
    }
    
    public ChatUtils setClickEvent(final et.a action, final String value) {
        this.style.a(new et(action, value));
        return this;
    }
    
    public ChatUtils setHoverEvent(final String value) {
        return this.setHoverEvent((eu)new fa(value));
    }
    
    public ChatUtils setHoverEvent(final eu value) {
        return this.setHoverEvent(ew.a.a, value);
    }
    
    public ChatUtils setHoverEvent(final ew.a action, final eu value) {
        this.style.a(new ew(action, value));
        return this;
    }
    
    public ChatUtils setInsertion(final String insertion) {
        this.style.a(insertion);
        return this;
    }
    
    public ChatUtils append(final String text) {
        return this.append(text, Inheritance.SHALLOW);
    }
    
    public ChatUtils append(final String text, final Inheritance inheritance) {
        return new ChatUtils(text, this.build(), inheritance);
    }
    
    public eu build() {
        final eu thisComponent = new fa(this.text).a(this.style);
        return (this.parent != null) ? this.parent.a(thisComponent) : thisComponent;
    }
    
    public enum Inheritance
    {
        DEEP, 
        SHALLOW, 
        NONE;
    }
}
