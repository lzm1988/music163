package top.ornobug.music163.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import top.ornobug.music163.domain.City;
import top.ornobug.music163.domain.Province;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CityUtil {

    private static List<Province> provinceList = null;

    static {
        File file = new File("E:\\ChinaCityList-master\\gov.json");
        try {
            String content = FileUtils.readFileToString(file, "UTF-8");
            Type listType = new TypeToken<ArrayList<Province>>() {
            }.getType();
            Gson gson = new Gson();
            provinceList = gson.fromJson(content,listType);
            System.out.println(gson.toJson(provinceList));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Province> getProvinceList(){
        return provinceList;
    }

    public static Province getProvince(String provinceCode){
        if (null != provinceList){
            for (Province province : provinceList ) {
                if (provinceCode.equals(province.getCode())){
                    return province;
                }
            }
        }
        return new Province();
    }

    public static List<City> getCityList(){
        List<City> cityList = new CopyOnWriteArrayList<City>();
        if (provinceList != null){
            for (Province province : provinceList ) {
                List<City> currentCityList = province.getCity();
                if (null != currentCityList){
                    cityList.addAll(currentCityList);
                }else {
                    System.out.println(province.getName());
                }
            }
        }
        return cityList;
    }

    public static City getCity(String cityCode){
        List<City> cityList = getCityList();
        if (null != cityList){
            for (City city : cityList ) {
                if (cityCode.equals(city.getCode())){
                    return city;
                }
            }
        }
        return new City();
    }

    public static void main(String[] args){

        City city = getCity("320100");
        System.out.println(city.getName());
    }

}
