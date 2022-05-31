import React from 'react'
import { useSelector } from 'react-redux'
import { useHistory } from 'react-router-dom'
import basicProfile from '../../images/basicProfile.svg'

function UserCard({user,setSearchUser}) {
    const auth = useSelector(state => state.auth)

    const history = useHistory()

    const handleOnClick = () =>{
        setSearchUser([])
        if(auth.user){
            history.push(`/user/${user.id}`)
        }else{
            history.push('/signin')
        }
    }
    return (
        <div className="header_search_user" onClick={handleOnClick}>
            <div className="header_search_user_image">
                <img src={user.imageUrl ? user.imageUrl : basicProfile} alt="" />
            </div>
            <div className="header_search_user_name">
                {user.firstname}{user.lastname}
            </div>
        </div>
    )
}

export default UserCard
