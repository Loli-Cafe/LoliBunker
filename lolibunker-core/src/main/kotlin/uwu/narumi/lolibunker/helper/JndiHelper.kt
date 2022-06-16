package uwu.narumi.lolibunker.helper

object JndiHelper {

    fun disableJndi() {
        System.setProperty("java.rmi.server.RMIClassLoaderSpi", "wjdf0qwuj90fj0oqwujf90 wquj90f jqw0o9f jk")
        System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "false")

        System.setProperty("log4j2.enableJndiLookup", "false")
        System.setProperty("log4j2.enableJndiContextSelector", "false")
        System.setProperty("log4j2.enableJndiJdbc", "false")
        System.setProperty("log4j2.enableJndiJms", "false")
    }
}