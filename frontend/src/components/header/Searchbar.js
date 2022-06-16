import axios from 'axios'
import React, { useEffect, useState } from 'react'
import { useHistory } from 'react-router'
import UserCard from './UserCard'

function Searchbar() {

    const [keyword, setKeyword] = useState('')
    const [filter, setFilter] = useState('user')
    const [searchUser, setSearchUser] = useState([])
    const [error, setError] = useState('')
    const [load, setLoad] = useState(false)

    const history = useHistory()

    useEffect(() => {
        if(!keyword){
            setSearchUser([])
        }
    }, [keyword])

    const handleSearch = async() =>{
        if(keyword){
            if(filter==="user"){
                setLoad(true)
                await axios.get(`/user/search/${keyword}`)
                            .then(res=> {console.log(res);setSearchUser(res.data)})
                            .catch(err=>setError(error.response.data.message))
                setLoad(false)
            }else if(filter==='post'){
                history.push(`/posts/search/${keyword}`)
            }else if(filter==='product'){
                history.push(`/products?keyword=${keyword}`)
            }
        }else{
            window.alert('Please enter keyword.')
        }
    }

    const handleErase = () =>{
        setSearchUser([])
        setKeyword('')
    }

    return (
        <div className="header_search">
            <select value={filter} onChange={(e)=>setFilter(e.target.value)}>
                <option value="user">USER</option>
                <option value="post">POST</option>
                <option value="product">PRODUCT</option>
            </select>
            <input type="text" value={keyword} onChange={e=>setKeyword(e.target.value)} />
            <i className="fas fa-eraser header_search_eraser"  onClick={handleErase}></i>
            <i className="far fa-search"  onClick={handleSearch}></i>
            {
                error &&
                <div className="header_search_error">
                    {error}
                </div>
            }

            {
                searchUser&&
                <div className="header_search_users">
                    {
                        load &&
                        <div>
                            loading...
                        </div>
                    }
                    {
                        searchUser.map((user, index)=>(
                                <UserCard user={user} key={index} setSearchUser={setSearchUser}/>
                        ))
                    }
                </div>
            }
        </div>
    )
}

export default Searchbar
