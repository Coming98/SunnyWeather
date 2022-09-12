# SunnyWeather

The android practice project in "First Line of Android" of Chapter 15.

# Tools

- 网络请求: Retrofit, Coroutines
- 数据解析: Gson

## Jetpack

- MMVM: Model-View-ViewModel

# Functional Requirement

- Support to obtain information of most cities in the world
- Support to obtain weather information of a specified city
- Support pull-down refresh

# Technique Feasibility Analysis

[彩云天气](https://dashboard.caiyunapp.com)
- City Information: `https://api.caiyunapp.com/v2/place?query={cityName}&token={token}`
- Weather Information of specificed city: `https://api.caiyunapp.com/v2.5/{token}/{longitude}, {latitude}/realtime.json`
- Weather Information of future: `https://api.caiyunapp.com/v2.5/{token}/{longitude}, {latitude}/daily.json`

# Description

## logic

存放业务逻辑相关的代码

- dao: 数据访问对象
- model: 对象模型
- network: 网络相关

## ui

存放界面展示相关的代码

- place: 
- weather: 

# Process

1. 分层架构下构建全局 Context 引用

## City Infos

![](https://raw.githubusercontent.com/Coming98/pictures/main/202209122206674.png)

### Logical Layer

1. 定义数据模型: logic/model/PlaceResponse
2. 定义网络服务接口: logic/network/PlaceService
3. 构建网络服务请求对象: logic/network/ServiceCreator
4. 定义一个统一的网络数据源访问入口，对所有网络请求的 API 进行封装: logic/network/SunnyWeatherNetwork
5. 仓库层代码: 判断调用方请求的数据应该是从本地数据源中获取还是从网络数据源中获取，并将获得的数据返回给调用方; local/Repository
6. 定义 ViewModel: 对界面上展示的数据进行封装维护; ui/place/placeViewModel
7. 声明网络权限

### UI Layer

1. res/layout/fragment_place.xml: 使用 fragment 而非 activity 是为了复用
2. res/layout/place_item.xml: 定义数据项（搜索结果中单条城市信息）布局
3. ui/place/placeAdapter: 定义 RecyclerView 的 adapter
4. ui/place/placeFragment: 实现对 Fragment 布局的加载