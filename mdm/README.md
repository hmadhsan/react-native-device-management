
# fareye-react-native-mdm

## Getting started

`$ npm install fareye-react-native-mdm --save`

### Mostly automatic installation

`$ react-native link fareye-react-native-mdm`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `fareye-react-native-mdm` and add `RNMdm.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNMdm.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNMdmPackage;` to the imports at the top of the file
  - Add `new RNMdmPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':fareye-react-native-mdm'
  	project(':fareye-react-native-mdm').projectDir = new File(rootProject.projectDir, 	'../node_modules/fareye-react-native-mdm/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':fareye-react-native-mdm')
  	```


## Usage
```javascript
import RNMdm from 'fareye-react-native-mdm';

// TODO: What to do with the module?
RNMdm;
```
  
