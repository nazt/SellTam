class Students 
{
	static belongsTo=[faculty:Faculty]
	String name
	String surname
	Boolean male
	
    static constraints = {
	name(unique:true,nullable:false,blank:false)
	surname()
	male()
    }
}
