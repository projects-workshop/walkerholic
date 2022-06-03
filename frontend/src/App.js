import {BrowserRouter , Route} from 'react-router-dom'
import Header from './components/header/Header';
import Footer from './components/Footer';
import HomeScreen from './screens/HomeScreen';
import LoginScreen from './screens/LoginScreen';
import RegisterScreen from './screens/RegisterScreen';
import ProfileScreen from './screens/ProfileScreen';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { authenticate } from './_actions/AuthActions';
import axios from 'axios';
import { GET_AUTH_FOLLOWS } from './_constants/AuthConstants';
import DiscoverScreen from './screens/DiscoverScreen';
import PostScreen from './screens/PostScreen';
import PostDetailScreen from './screens/PostDetailScreen';
import ActivityScreen from './screens/ActivityScreen';
import ActivityDetailScreen from './screens/ActivityDetailScreen';
import PostsScreen from './screens/PostsScreen';
import ProductScreen from './screens/ProductScreen'
import ProductDetailScreen from './screens/ProductDetailScreen'
import PlaceOrderScreen from './screens/PlaceOrderScreen';
import AboutUsScreen from './screens/AboutUsScreen';
import OrderListScreen from './screens/OrderListScreen';
import { getCart } from './_actions/OrderActions';
import UserActivityScreen from './screens/UserActivityScreen';
import ProductListScreen from './screens/ProductListScreen';
import UserListScreen from './screens/UserListScreen';
import OrderScreen from './screens/OrderScreen';
import ForgotPasswordScreen from './screens/ForgotPasswordScreen';

function App() {
  
  axios.defaults.baseURL = "https://walkerholic.n-e.kr/api";
  const auth = useSelector(state => state.auth)

  const dispatch = useDispatch()
  
  useEffect(() => {
    if(localStorage.getItem("walkerholic_token")&& !auth.user){
      const token = localStorage.getItem("walkerholic_token")
      dispatch(authenticate(token)).then(async(id)=>{
        const res1 = await axios.get(`/follows/${id}`,{
          headers : {Authorization : `Bearer ${token}`}
        })
        dispatch({
          type:GET_AUTH_FOLLOWS,
          payload:res1.data
        })
        dispatch(getCart(id))
      })
    }
  }, [auth.user])

  return (
    <BrowserRouter>
      <div className="App">
          <Header/>

          <div className="main">
            <Route exact path="/" component={HomeScreen}/>
            <Route exact path="/oauth2/redirect" component={HomeScreen}/>

            <Route exact path="/signin" component={LoginScreen}/>
            <Route exact path="/forgotPassword" component={ForgotPasswordScreen}/>
            <Route exact path="/signup" component={RegisterScreen}/>
            <Route exact path="/user/:id" component={ProfileScreen}/>
            <Route exact path="/userlist" component={UserListScreen}/>

            <Route exact path="/posts" component={PostsScreen}/>
            <Route exact path="/posts/search/:keyword" component={PostsScreen}/>
            <Route exact path="/posts/discover" component={DiscoverScreen}/>
            <Route exact path="/posts/user/:id" component={PostScreen}/>
            <Route exact path="/post/:id" component={PostDetailScreen}/>
            <Route exact path="/activities" component={ActivityScreen}/>
            <Route exact path="/activity/:id" component={ActivityDetailScreen}/>
            <Route exact path="/activities/user/:userId" component={UserActivityScreen}/>

            <Route exact path="/products" component={ProductScreen}/>
            <Route exact path="/products/:seller" component={ProductScreen}/>
            <Route exact path="/product/:id" component={ProductDetailScreen}/>
            <Route exact path="/placeorder/:id" component={PlaceOrderScreen}/>
            <Route exact path="/productlist" component={ProductListScreen}/>
            <Route exact path="/productlist/:sellerId" component={ProductListScreen}/>


            <Route exact path="/about" component={AboutUsScreen}/>

            <Route exact path="/order/:id" component={OrderScreen}/>
            <Route exact path="/orderlist" component={OrderListScreen}/>
            <Route exact path="/orderlist/:id" component={OrderListScreen}/>
            <Route exact path="/orderlist/user/:userId" component={OrderListScreen}/>

          </div>
          <Footer/>
      </div>
    </BrowserRouter>
  );
}

export default App;
