package dev.cudzer.cobblemonsizevariation;

public class ModDependencyChecker {

    private final Platform platform;

    public boolean IsCobblemonRideOnInstalled;

    public ModDependencyChecker(Platform platform){
        this.platform = platform;
    }


    public void checkDependencies(){
        IsCobblemonRideOnInstalled = platform.isModInstalled("cobbleride");
    }
}
