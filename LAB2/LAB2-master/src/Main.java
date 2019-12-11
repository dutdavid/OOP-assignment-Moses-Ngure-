public class Main {

    public static void main(String[] args){

        Sedan sedan1 = new Sedan("Toyota",5,4);

        /*
         method overloading:  multiple methods having same name but different in parameters
        */
        sedan1.accelerate();
        sedan1.accelerate(15);

        /*
       A subclass provides the specific implementation of the method
       that has been declared by one of its parent class
       */
    }
}
