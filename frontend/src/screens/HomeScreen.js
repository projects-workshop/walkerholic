import React, { useEffect, useState } from 'react'
import Main from '../images/Main.jpg'
import { useDispatch, useSelector } from 'react-redux';
import { getProducts } from '../_actions/ProductActions';
import ProductCard from '../components/home/ProductCard';
import { Link } from 'react-router-dom';
import { getHomePost } from '../_actions/PostActions';
import HomePostCard from '../components/home/HomePostCard';
import { auth } from '../_actions/AuthActions';
import axios from 'axios';
import { GET_AUTH_FOLLOWS } from '../_constants/AuthConstants';
import { getCart } from '../_actions/OrderActions';
import Error from '../components/Error';
import Loading from '../components/Loading';

function HomeScreen(props) {

    const [products, setProducts] = useState([])
    const [posts, setPosts] = useState([])
    const [isLoad, setIsLoad] = useState(true)

    const postsErr = useSelector(state => state.home.error?.message)
    const productsErr = useSelector(state => state.products.error?.message)
    const authErr = useSelector(state => state.auth.error?.message)

    const token = props.location.search.substr(7)


    const dispatch = useDispatch()
    useEffect(() => {
        if(token){
            localStorage.setItem("walkerholic_token", token)
            dispatch(auth(token)).then(async(id)=>{
                const res1 = await axios.get(`/follows/${id}`,{
                    headers : {Authorization : `Bearer ${token}`}
                })
                dispatch({
                  type:GET_AUTH_FOLLOWS,
                  payload:res1.data
                })
                // const res2 = await axios.get(`/cartItems/${id}`)
                // dispatch({
                //   type:GET_CARTITEMS_SUCCESS,
                //   payload:res2.data
                // })
                dispatch(getCart(id))
              })
        }
        setIsLoad(true)
        dispatch(getProducts(1)).then(res=>
            setProducts(res)
        )
        dispatch(getHomePost(1, 'popular')).then(res=>{
            setPosts(res)
        })
        setIsLoad(false)
    }, [dispatch])

    return (
        <div className="home">
            {
                (postsErr || productsErr || authErr)&& <Error error = {postsErr+ productsErr + authErr}/>
            }
            {
                isLoad && <Loading/>
            }
            <div className="home_main">
                <div className="home_maintitle">
                    Save<br/><span style={{backgroundColor:"#b59760", fontStyle:"italic"}}>&nbsp;our&nbsp;</span><br/>planet
                </div>
                <div className="home_image">
                    <div className="home_image_description">
                        <strong>7, May, 2021 </strong> Don't eliminate preciousness
                    </div>
                    <img src={Main} alt="" />
                </div>
            </div>
            <div className="home_products_container">
                <div className="home_more_icon" >
                    <Link to="/products">More Products <i className="fas fa-arrow-right"></i></Link>
                </div>
                <div className="home_products">
                {
                    !isLoad && products &&
                    products.map((product, index)=>(
                        <ProductCard product={product} key={index}/>
                    ))                
                }
                </div>
            </div>
            <div className="home_posts_tape_container">
                <div className="home_posts_tape">
                    <div className="home_posts_tape_top_phrase">
                        <p>
                            Checkout our planetsaver's latest posts Share idea to save earth Checkout our planetsaver's latest posts Share idea to save earth
                        </p>
                    </div>
                </div>
                <div className="home_posts_container">
                    <div className="home_posts_description">
                        <div className="home_posts_description_more_button">
                            <Link to="/posts">→ More Posts</Link>
                        </div>
                        Latest <br/>popular <br/> posts
                    </div>
                    <div className="home_posts">
                    {
                        !isLoad &&
                        posts.map((post, index)=>(
                            <HomePostCard post={post} key={index}/>
                        ))
                    }
                    </div>
                </div>
                <div className="home_posts_tape">
                    <div className="home_posts_tape_bottom_phrase">
                        <p>
                            Checkout our planetsaver's latest posts Share idea to save earth Checkout our planetsaver's latest posts Share idea to save earth
                        </p>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default HomeScreen
