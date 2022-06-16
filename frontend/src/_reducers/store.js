import{combineReducers, createStore, compose, applyMiddleware} from 'redux';
import thunk from 'redux-thunk'
import { activityReducer } from './ActivityReducers';
import { authReducer } from './AuthReducers';
import { listReducer } from './ListReducers';
import { cartReducer, orderReducer } from './OrderReducers';
import { discoverReducer, followingPostsReducer, homeReducer, postReducer } from './PostReducers';
import { productsReducer } from './ProductReducers';
import { profileReducer } from './ProfileReducers';

const initialState = {

}
// const initialState={
//     auth : {
//         ...localStorage.getItem('token')? {token:JSON.parse(localStorage.getItem('token')} : null
//     }
// }

const reducer = combineReducers({
    auth : authReducer,
    profile : profileReducer,
    discover : discoverReducer,
    posts : followingPostsReducer,
    post : postReducer,
    home : homeReducer,
    activity : activityReducer,
    products : productsReducer,
    order : orderReducer,
    cart : cartReducer,
    list: listReducer,
    

})

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__||compose;
const store = createStore(reducer, initialState, composeEnhancer(applyMiddleware(thunk)));
export default store;