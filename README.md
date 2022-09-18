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
3. 初始化构建网络服务请求对象: logic/network/ServiceCreator
4. 定义一个统一的网络数据源访问入口，对所有网络请求的 API 进行封装: logic/network/SunnyWeatherNetwork
5. 仓库层代码: 判断调用方请求的数据应该是从本地数据源中获取还是从网络数据源中获取，并将获得的数据返回给调用方; local/Repository
6. 定义 ViewModel: 对界面上展示的数据进行封装维护; ui/place/placeViewModel
7. 声明网络权限

### UI Layer

1. res/layout/fragment_place.xml: 使用 fragment 而非 activity 是为了复用
2. res/layout/place_item.xml: 定义数据项（搜索结果中单条城市信息）布局
3. ui/place/placeAdapter: 定义 RecyclerView 的 adapter
4. ui/place/placeFragment: 实现对 Fragment 布局的加载

## Weather Infos By City

Target: 
1. 获取指定的城市信息
2. 查询目标城市的天气信息
3. 显示天气信息

![](https://raw.githubusercontent.com/Coming98/pictures/main/202209151406150.png)

### Logical Layer

1. 定义数据模型: logic/model/RealtimeResponse.kt + logic/model/DailyResponse.kt
2. 进一步封装上述数据模型，方便使用(这是是因为选择查询城市天气时同时响应实时天气信息 Realtime 与未来天气信息 Daily, 所有进一步封装数据结构): logic/model/Weather
3. 定义访问目标城市天气数据的网络接口: logic/network/WeatherService
4. 更新网络数据源访问入口，添加目标城市天气数据的访问接口: Update -> logic/network/SunnyWeatherNetwork
5. 更新仓库层代码，获取天气数据并返回给调用方：Update -> local/Repository
6. 定义 ViewModel: ui/weather/WeatherViewModel

### UI layer

1. layout/activity_weather.xml: 构建布局 <<< now + forecast + forecast_item + life_index
2. ui/weather/WeatherActivity: 处理数据，展示天气信息
3. [TODO] - 沉浸式状态栏

### 持久化选中的地址

1. logic/dao/PlaceDao: 持久化选中的地址
2. logic/Repository: 完善地址数据的获取与存储逻辑处理 - [TODO] 线程异步获取 + ViewModel 的封装
3. ui/place/PlaceViewModel: 面向 UI 的数据处理接口
4. ui/place/PlaceAdapter: UI 上的逻辑处理，持久化选中的城市信息
5. ui/place/PlaceFragment: 页面展示的逻辑处理，如果检测到有本地化的城市信息则读取并跳转 activity

## Refresh Weather Info

使用下拉刷新控件实现 SwipeRefreshLayout

### UI Layer

1. layout/activity_weather.xml: 添加下拉控件 SwipeRefreshLayout
2. ui/weather/weather_activity: 书写逻辑

## Change City

使用滑动菜单布局城市索引 Fragment

![](https://raw.githubusercontent.com/Coming98/pictures/main/202209181157938.png)

### UI Layer

1. layout/now.xml: 添加触发滑动菜单的显示交互按钮
2. layout/activity_weather.xml: 加入滑动菜单
3. ui/weather/weather_activity: 书写逻辑
4. ui/place/PlaceFragment: 完善 PlaceFragment 的跳转逻辑
5. ui/place/PlaceAdapter: 完善 PlaceAdapter 的选定城市跳转逻辑