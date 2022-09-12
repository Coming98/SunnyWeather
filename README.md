# SunnyWeather

The android practice project in "First Line of Android" of Chapter 15.

# Tools

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
2. 定义数据模型: data model of city
3. 获取数据: 网络层获取 city data
