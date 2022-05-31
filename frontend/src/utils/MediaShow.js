import Starter from '../images/Starter.svg'
import Bronze from '../images/Bronze.svg'
import Silver from '../images/Silver.svg'
import Gold from '../images/Gold.svg'
import Master from '../images/Master.svg'

export const imageShow = (src) =>{
    return <img src={src} alt="images" className="img-thumbnail" />
}

export const videoShow = (src) =>{
    return <video src={src} alt="images" className="img-thumbnail" ></video>
}

export const levelIconShow = (level) =>{
    switch (level) {
        // case 'Starter' :
        //     return <img src={Starter} alt="level" />
        case 'Bronze' :
            return <img className="level_icon" src={Bronze} alt="level" />
        case 'Silver' :
            return <img className="level_icon" src={Silver} alt="level" />
        case 'Gold' :
            return <img className="level_icon" src={Gold} alt="level" />
        case 'Master' :
            return <img className="level_icon" src={Master} alt="level" />
        default :
            return <img className="level_icon" src={Starter} alt="level" />

    }
}

export const levelDescriptionIconShow = (level) =>{
    switch (level) {
        // case 'Starter' :
        //     return <img src={Starter} alt="level" />
        case 'Bronze' :
            return <img className="level_description_icon" src={Bronze} alt="level" />
        case 'Silver' :
            return <img className="level_description_icon" src={Silver} alt="level" />
        case 'Gold' :
            return <img className="level_description_icon" src={Gold} alt="level" />
        case 'Master' :
            return <img className="level_description_icon" src={Master} alt="level" />
        default :
            return <img className="level_description_icon" src={Starter} alt="level" />

    }
}