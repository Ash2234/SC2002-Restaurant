To compile:
```cmd
javac -d out -sourcepath src/main/java src/main/java/restaurant/RestaurantRushApp.java
```

To run:
```cmd
java -cp out restaurant.RestaurantRushApp
```

To build docs:
```cmd
javadoc -d docs -sourcepath src/main/java -subpackages restaurant -private
```

To view docs either run index.html manually or just run the command:
```cmd
start docs\index.html
```
